package fr.hardcoding.java.chat;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;

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
    }

    public void add(ChatMessage message) {
        this.messages.add(message);
    }

    public List<ChatMessage> list() {
        return this.messages;
    }
}
