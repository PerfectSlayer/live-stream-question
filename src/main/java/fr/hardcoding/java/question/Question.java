package fr.hardcoding.java.question;

import java.util.UUID;

/**
 * Question resource.
 *
 * @author Bruce BUJON (bruce.bujon(at)gmail(dot)com)
 */
public class Question {
    public UUID uuid;
    public String userName;
    public String text;
    public String profileUrl;
    public boolean answered;

    public Question() {

    }

    public Question(UUID uuid, String userName, String text, String profileUrl, boolean answered) {
        this.uuid = uuid;
        this.userName = userName;
        this.text = text;
        this.profileUrl = profileUrl;
        this.answered = answered;
    }
}
