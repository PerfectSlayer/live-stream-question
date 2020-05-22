package fr.hardcoding.java.chat;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * This class stores all chat messages.
 *
 * @author Bruce BUJON (bruce.bujon(at)gmail(dot)com)
 */
@ApplicationScoped
public class ChatModel {

    private final List<ChatMessage> messages;

    public ChatModel() {
        this.messages = new ArrayList<>();
        addSample();
    }

    public void addAll(List<ChatMessage> messages) {
        this.messages.addAll(messages);
    }

    public List<ChatMessage> list() {
        return this.messages;
    }

    public Optional<ChatMessage> getFromId(UUID uuid) {
        return this.messages.stream()
                .filter(chatMessage -> chatMessage.uuid.equals(uuid))
                .findAny();
    }

    private void addSample() {
        addAll(List.of(
                new ChatMessage(
                        UUID.randomUUID(),
                        "Test de message",
                        "Bruce BUJON",
                        "https://yt3.ggpht.com/-dhsyBDXOnaU/AAAAAAAAAAI/AAAAAAAAAAA/fEaE_-bKlOc/s88-c-k-no-mo-rj-c0xffffff/photo.jpg"
                ), new ChatMessage(
                        UUID.randomUUID(),
                        "Test 2",
                        "Bruce BUJON",
                        "https://yt3.ggpht.com/-dhsyBDXOnaU/AAAAAAAAAAI/AAAAAAAAAAA/fEaE_-bKlOc/s88-c-k-no-mo-rj-c0xffffff/photo.jpg"
                )
        ));
    }
}
