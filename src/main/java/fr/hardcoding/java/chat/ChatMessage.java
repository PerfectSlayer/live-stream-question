package fr.hardcoding.java.chat;

import java.util.UUID;

/**
 * Chat message resource.
 *
 * @author Bruce BUJON (bruce.bujon(at)gmail(dot)com)
 */
public class ChatMessage {
    public UUID uuid;
    public String userName;
    public String text;
    public String profileUrl;

    public ChatMessage() {

    }

    public ChatMessage(UUID uuid, String userName, String text, String profileUrl) {
        this.uuid = uuid;
        this.userName = userName;
        this.text = text;
        this.profileUrl = profileUrl;
    }
}
