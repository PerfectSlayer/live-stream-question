package fr.hardcoding.java.chat.twitch;

import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.chat.TwitchChat;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.github.twitch4j.common.events.domain.EventUser;
import com.github.twitch4j.helix.TwitchHelix;
import com.github.twitch4j.helix.domain.User;
import com.github.twitch4j.helix.domain.UserList;
import fr.hardcoding.java.chat.ChatMessage;
import fr.hardcoding.java.chat.ChatModel;
import fr.hardcoding.java.chat.ChatSocket;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.Optional.empty;

public class TwitchChatFetcher {
    private static final Logger LOGGER = Logger.getLogger(TwitchChatFetcher.class.getName());
    @ConfigProperty(name = "twitch.chat.enabled", defaultValue = "false")
    boolean featureEnabled;
    @ConfigProperty(name = "twitch.client.id", defaultValue = "")
    String clientId;
    @ConfigProperty(name = "twitch.client.secret", defaultValue = "")
    String clientSecret;
    @ConfigProperty(name = "twitch.access.token", defaultValue = "")
    String accessToken;
    @ConfigProperty(name = "twitch.channel", defaultValue = "")
    String channel;
    @Inject
    ChatModel model;
    @Inject
    ChatSocket socket;
    private TwitchChat chat;
    private TwitchHelix helix;
    private final Map<String, User> userDetails = new HashMap<>();

    void onStart(@Observes StartupEvent ev) {
        if (!this.featureEnabled) {
            LOGGER.info("Twitch chat feature is disabled.");
            return;
        }
        if (this.clientId.isBlank() || this.clientSecret.isBlank()) {
            LOGGER.warning("Twitch client id or secret is missing.");
            return;
        }
        if (this.accessToken.isBlank()) {
            LOGGER.warning("Twitch oauth token is missing.");
            return;
        }
        if (this.channel.isBlank()) {
            LOGGER.warning("Twitch channel is missing.");
            return;
        }
        LOGGER.info("Starting Twitch chat fetcher...");
        // Create credential manager
        TwitchClient client = TwitchClientBuilder.builder()
                .withClientId(this.clientId)
                .withClientSecret(this.clientSecret)
                .withEnableChat(true)
                .withEnableHelix(true)
                .build();
        this.helix = client.getHelix();
        this.chat = client.getChat();
        this.chat.getEventManager().onEvent(ChannelMessageEvent.class, event -> {
            ChatMessage chatMessage = convert(event);
            this.model.add(chatMessage);
            this.socket.send(chatMessage);
        });
        this.chat.joinChannel(this.channel);
    }

    private ChatMessage convert(ChannelMessageEvent event) {
        UUID uuid = UUID.randomUUID();
        String text = event.getMessage();
        User userDetails = getUserDetails(event.getUser());
        String userName = userDetails.getDisplayName();
        String profileUrl = userDetails.getProfileImageUrl();
        return new ChatMessage(uuid, userName, text, profileUrl);
    }

    private User getUserDetails(EventUser user) {
        String userId = user.getId();
        // Get details from cache
        User userDetails = this.userDetails.get(userId);
        if (userDetails == null) {
            // Fetch user details and cache them
            Optional<User> userDetailsOptional = fetchUserDetails(userId);
            if (userDetailsOptional.isPresent()) {
                userDetails = userDetailsOptional.get();
                this.userDetails.put(userId, userDetails);
            }
        }
        if (userDetails == null) {
            // Get default details
            userDetails = getUserNotFoundDetails(user);
        }
        return userDetails;
    }

    private Optional<User> fetchUserDetails(String userId) {
        try {
            UserList users = this.helix.getUsers(this.accessToken, List.of(userId), null).execute();
            return users.getUsers().stream()
                    .filter(u -> userId.equals(u.getId()))

                    .findAny();
        } catch (Throwable t) {
            LOGGER.log(Level.WARNING, "Failed to get user (id = " + userId + ") details.", t);
            return empty();
        }
    }

    private User getUserNotFoundDetails(EventUser user) {
        return new User() {
            @Override
            public String getId() {
                return user.getId();
            }

            @Override
            public String getDisplayName() {
                return user.getName();
            }

            @Override
            public String getProfileImageUrl() {
                return "/img/twitch.jpg";
            }
        };
    }

    void onStop(@Observes ShutdownEvent ev) {
        if (this.chat != null) {
            this.chat.disconnect();
        }
    }
}
