package fr.hardcoding.java.chat;

/**
 * Chat message resource.
 *
 * @author Bruce BUJON (bruce.bujon(at)gmail(dot)com)
 */
public class ChatMessage {
    public String userName;
    public String text;
    public String profileUrl;

    public ChatMessage() {

    }

    public ChatMessage(String userName, String text, String profileUrl) {
        this.userName = userName;
        this.text = text;
        this.profileUrl = profileUrl;
    }
}
