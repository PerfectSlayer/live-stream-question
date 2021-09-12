package fr.hardcoding.java.question;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * This class stores all questions.
 *
 * @author Bruce BUJON (bruce.bujon(at)gmail(dot)com)
 */
@ApplicationScoped
public class QuestionModel {
    private final List<Question> questions;
    private Question promoted;

    public QuestionModel() {
        this.questions = new ArrayList<>();
        addHelpData();
    }

    public void add(Question question) {
        this.questions.add(question);
    }

    public List<Question> list() {
        return this.questions;
    }

    public boolean remove(UUID uuid) {
        if (this.promoted != null && this.promoted.uuid.equals(uuid)) {
            return false;
        }
        return this.questions.removeIf(question -> question.uuid.equals(uuid));
    }

    public Optional<Question> getPromoted() {
        return Optional.ofNullable(this.promoted);
    }

    public boolean promote(UUID uuid) {
        Optional<Question> questionOptional = this.questions.stream()
                .filter(question -> question.uuid.equals(uuid))
                .findAny();
        if (questionOptional.isEmpty()) {
            return false;
        }
        this.promoted = questionOptional.get();
        return true;
    }

    public void demote() {
        this.promoted.answered = true;
        this.promoted = null;
    }

    public boolean toggleAnswered(UUID uuid) {
        Optional<Question> questionOptional = this.questions.stream()
                .filter(question -> question.uuid.equals(uuid))
                .findAny();
        if (questionOptional.isEmpty()) {
            return false;
        }
        questionOptional.get().answered = !questionOptional.get().answered;
        return true;
    }

    private void addHelpData() {
        Question question = new Question(
                UUID.fromString("a5ef1d1b-4b5c-4c62-93bb-9ec589d3fadb"),
                "Help bot",
                "All pinned questions are saved here. You can promote one to show it in the live stream using the <i class=\"far fa-bookmark fa-lg\"></i> promote button or discard them the <i class=\"fas fa-trash-alt fa-lg\"></i> discard button. Note you can only promote one question at a time.",
                "/img/bot.png",
                true
        );
        this.questions.add(question);
        question = new Question(
                UUID.fromString("48bed050-8a5d-4b3d-b0cf-77a897fca175"),
                "Help bot",
                "You can also manually add some questions using for form below. <i class=\"fas fa-arrow-down\"></i>",
                "/img/bot.png",
                true
        );
        this.questions.add(question);
    }
}
