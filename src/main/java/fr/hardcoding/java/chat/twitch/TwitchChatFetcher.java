package fr.hardcoding.java.chat.twitch;

import com.github.philippheuer.credentialmanager.CredentialManager;
import com.github.philippheuer.credentialmanager.CredentialManagerBuilder;
import com.github.twitch4j.auth.providers.TwitchIdentityProvider;
import com.github.twitch4j.chat.TwitchChat;
import com.github.twitch4j.chat.TwitchChatBuilder;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import fr.hardcoding.java.chat.ChatMessage;
import fr.hardcoding.java.chat.ChatModel;
import fr.hardcoding.java.chat.ChatSocket;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.UUID;
import java.util.logging.Logger;

public class TwitchChatFetcher {
    private static final Logger LOGGER = Logger.getLogger(TwitchChatFetcher.class.getName());
    @ConfigProperty(name = "twitch.chat.enabled", defaultValue = "false")
    boolean featureEnabled;
    @ConfigProperty(name = "twitch.client.id", defaultValue = "")
    String clientId;
    @ConfigProperty(name = "twitch.client.secret", defaultValue = "")
    String clientSecret;
    @ConfigProperty(name = "twitch.channel", defaultValue = "")
    String channel;
    @Inject
    ChatModel model;
    @Inject
    ChatSocket socket;
    private TwitchChat client;

    void onStart(@Observes StartupEvent ev) {
        if (!this.featureEnabled) {
            LOGGER.info("Twitch chat feature is disabled.");
            return;
        }
        if (this.clientId.isBlank() || this.clientSecret.isBlank()) {
            LOGGER.warning("Twitch client id or secret is missing.");
            return;
        }
        if (this.channel.isBlank()) {
            LOGGER.warning("Twitch channel is missing.");
            return;
        }
        LOGGER.info("Starting Twitch chat fetcher...");
        // Create credential manager
        CredentialManager credentialManager = CredentialManagerBuilder.builder().build();
        credentialManager.registerIdentityProvider(
                new TwitchIdentityProvider("ydaymxd690kuww00wjsyhcbxqww7hv", "8nqok9ozgwd4nh53z6g2gt5xh0akfl", "")
        );
        // Create client and join channel
        this.client = TwitchChatBuilder.builder()
                .withCredentialManager(credentialManager)
                .build();
        this.client.getEventManager().onEvent(ChannelMessageEvent.class, event -> {
            ChatMessage chatMessage = convert(event);
            this.model.add(chatMessage);
            this.socket.send(chatMessage);
        });
        this.client.joinChannel(this.channel);
    }

    void onStop(@Observes ShutdownEvent ev) {
        if (this.client != null) {
            this.client.disconnect();
        }
    }

    private static ChatMessage convert(ChannelMessageEvent event) {
        UUID uuid = UUID.randomUUID();
        String text = event.getMessage();
        String userName = event.getUser().getName();
        String profileUrl = "/img/twitch.jpg";
        return new ChatMessage(uuid, userName, text, profileUrl);
    }
}
