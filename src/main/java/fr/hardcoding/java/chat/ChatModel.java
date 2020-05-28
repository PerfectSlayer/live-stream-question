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
        addHelpData();
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

    private void addHelpData() {
        addAll(List.of(
                new ChatMessage(
                        UUID.randomUUID(),
                        "Help bot",
                        "Here are the YouTube Live messages. You can pin any interesting question using the <i class=\"fas fa-thumbtack fa-lg\"></i> pin icon.",
                        "/img/bot.png"
                )
        ));
    }
}
