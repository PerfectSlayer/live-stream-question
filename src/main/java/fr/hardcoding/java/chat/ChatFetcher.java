package fr.hardcoding.java.chat;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.LiveBroadcast;
import com.google.api.services.youtube.model.LiveBroadcastListResponse;
import com.google.api.services.youtube.model.LiveChatMessage;
import com.google.api.services.youtube.model.LiveChatMessageAuthorDetails;
import com.google.api.services.youtube.model.LiveChatMessageListResponse;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import com.google.common.collect.Lists;
import io.quarkus.runtime.StartupEvent;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static com.google.api.services.youtube.YouTubeScopes.YOUTUBE_READONLY;
import static java.lang.Math.max;
import static java.util.logging.Level.WARNING;

/**
 * This class periodically fetch live chat message to store them to model and send them through web socket.
 *
 * @author Bruce BUJON (bruce.bujon(at)gmail(dot)com)
 */
public class ChatFetcher {
    private static final Logger LOGGER = Logger.getLogger(ChatFetcher.class.getName());
    /**
     * Common fields to retrieve for chat messages
     */
    private static final String LIVE_CHAT_FIELDS =
            "items(authorDetails(displayName,profileImageUrl),snippet(displayMessage))," +
                    "nextPageToken," +
                    "pollingIntervalMillis";
    /**
     * The minimum delay between two API polls in milliseconds.
     */
    private static final long MIN_POLL_DELAY_MILLIS = 30 * 1000;
    /**
     * The chat feature flag.
     */
    @ConfigProperty(name = "video.chat", defaultValue = "true")
    boolean featureEnabled;
    /**
     * The video identifier.
     */
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    @ConfigProperty(name = "video.id")
    Optional<String> videoId;
    @Inject
    ChatModel model;
    @Inject
    ChatSocket socket;
    /**
     * Define a global instance of a Youtube object, which will be used
     * to make YouTube Data API requests.
     */
    private YouTube youtube;


    void onStart(@Observes StartupEvent ev) {
        if (!this.featureEnabled) {
            LOGGER.info("Chat feature is disabled.");
            return;
        }
        LOGGER.info("The application is starting...");
        // This OAuth 2.0 access scope allows for read-only access to the
        // authenticated user's account, but not other types of account access.
        List<String> scopes = Lists.newArrayList(YOUTUBE_READONLY);

        try {
            // Authorize the request.
            Credential credential = Auth.authorize(scopes, "listlivechatmessages");

            // This object is used to make YouTube Data API requests.
            this.youtube = new YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, credential)
                    .setApplicationName("youtube-live-stream-question").build();

            // Check the liveChatId
            String liveChatId = this.videoId.isEmpty() ?
                    getLiveChatId(youtube) :
                    getLiveChatId(youtube, this.videoId.get());
            if (liveChatId == null) {
                LOGGER.severe("Unable to find a live chat id.");
                System.exit(1);
            }

            // Get live chat messages
            listChatMessages(liveChatId, null, 0);
        } catch (GoogleJsonResponseException e) {
            LOGGER.log(WARNING, "GoogleJsonResponseException code: " + e.getDetails().getCode() + " : " + e.getDetails().getMessage(), e);
        } catch (IOException e) {
            LOGGER.log(WARNING, "IOException: " + e.getMessage(), e);
        } catch (Throwable t) {
            LOGGER.log(WARNING, "Throwable: " + t.getMessage(), t);
        }
    }

    /**
     * Retrieves the liveChatId from the authenticated user's live broadcast.
     *
     * @param youtube The object is used to make YouTube Data API requests.
     * @return A liveChatId, or null if not found.
     */
    private static String getLiveChatId(YouTube youtube) throws IOException {
        // Get signed in user's liveChatId
        YouTube.LiveBroadcasts.List broadcastList = youtube
                .liveBroadcasts()
                .list("snippet")
                .setFields("items/snippet/liveChatId")
                .setBroadcastType("all")
                .setBroadcastStatus("active");
        LiveBroadcastListResponse broadcastListResponse = broadcastList.execute();
        for (LiveBroadcast b : broadcastListResponse.getItems()) {
            String liveChatId = b.getSnippet().getLiveChatId();
            if (liveChatId != null && !liveChatId.isEmpty()) {
                return liveChatId;
            }
        }

        return null;
    }

    /**
     * Retrieves the liveChatId from the broadcast associated with a videoId.
     *
     * @param youtube The object is used to make YouTube Data API requests.
     * @param videoId The videoId associated with the live broadcast.
     * @return A liveChatId, or null if not found.
     */
    private String getLiveChatId(YouTube youtube, String videoId) throws IOException {
        // Get liveChatId from the video
        YouTube.Videos.List videoList = youtube.videos()
                .list("liveStreamingDetails")
                .setFields("items/liveStreamingDetails/activeLiveChatId")
                .setId(videoId);
        VideoListResponse response = videoList.execute();
        for (Video v : response.getItems()) {
            String liveChatId = v.getLiveStreamingDetails().getActiveLiveChatId();
            if (liveChatId != null && !liveChatId.isEmpty()) {
                return liveChatId;
            }
        }

        return null;
    }

    /**
     * Lists live chat messages, polling at the server supplied interval. Owners and moderators of a
     * live chat will poll at a faster rate.
     *
     * @param liveChatId    The live chat id to list messages from.
     * @param nextPageToken The page token from the previous request, if any.
     * @param delayMs       The delay in milliseconds before making the request.
     */
    private void listChatMessages(
            final String liveChatId,
            final String nextPageToken,
            long delayMs
    ) {
        LOGGER.fine(String.format("Getting chat messages in %1$.3f seconds...", delayMs * 0.001));
        Timer pollTimer = new Timer();
        pollTimer.schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        try {
                            // Get chat messages from YouTube
                            LiveChatMessageListResponse response = youtube
                                    .liveChatMessages()
                                    .list(liveChatId, "snippet, authorDetails")
                                    .setPageToken(nextPageToken)
                                    .setFields(LIVE_CHAT_FIELDS)
                                    .execute();

                            // Display messages and super chat details
                            List<ChatMessage> messages = response.getItems()
                                    .stream()
                                    .map(ChatFetcher.this::convert)
                                    .collect(Collectors.toList());
                            ChatFetcher.this.model.addAll(messages);
                            messages.forEach(ChatFetcher.this.socket::send);

                            // Request the next page of messages
                            listChatMessages(
                                    liveChatId,
                                    response.getNextPageToken(),
                                    max(MIN_POLL_DELAY_MILLIS, response.getPollingIntervalMillis())
                            );
                        } catch (Throwable t) {
                            LOGGER.log(WARNING, "Throwable: " + t.getMessage(), t);
                        }
                    }
                }, delayMs);
    }

    private ChatMessage convert(LiveChatMessage liveChatMessage) {
        UUID uuid = UUID.randomUUID();
        String text = liveChatMessage.getSnippet().getDisplayMessage();
        LiveChatMessageAuthorDetails authorDetails = liveChatMessage.getAuthorDetails();
        String userName = authorDetails.getDisplayName();
        String profileUrl = authorDetails.getProfileImageUrl();
        return new ChatMessage(uuid, userName, text, profileUrl);
    }
}
