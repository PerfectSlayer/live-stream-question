package fr.hardcoding.java.question;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * This class stores all questions.
 *
 * @author Bruce BUJON (bruce.bujon(at)gmail(dot)com)
 */
@ApplicationScoped
public class QuestionModel {
    private static final Comparator<Question> QUESTION_SORT = Comparator.comparing(question -> question.uuid);
    private final Map<UUID, Question> questions;
    private Question promoted;

    public QuestionModel() {
        this.questions = new HashMap<>();
        addHelpData();
    }

    public void add(Question question) {
        this.questions.put(question.uuid, question);
    }

    public List<Question> list() {
        List<Question> sortedQuestions = new ArrayList<>(this.questions.values());
        sortedQuestions.sort(QUESTION_SORT);
        return sortedQuestions;
    }

    public boolean remove(UUID uuid) {
        if (this.promoted != null && this.promoted.uuid.equals(uuid)) {
            return false;
        }
        return this.questions.remove(uuid) != null;
    }

    public Optional<Question> getPromoted() {
        return Optional.ofNullable(this.promoted);
    }

    public boolean promote(UUID uuid) {
        Question question = this.questions.get(uuid);
        if (question == null) {
            return false;
        }
        this.promoted = question;
        return true;
    }

    public void demote() {
        this.promoted = null;
    }

    private void addHelpData() {
        Question question = new Question(
                UUID.fromString("a5ef1d1b-4b5c-4c62-93bb-9ec589d3fadb"),
                "Help bot",
                "All pinned questions are saved here. You can promote one to show it in the live stream using the <i class=\"far fa-bookmark fa-lg\"></i> promote button or discard them the <i class=\"fas fa-trash-alt fa-lg\"></i> discard button. Note you can only promote one question at a time.",
                "/img/bot.png"
        );
        this.questions.put(question.uuid, question);
        question = new Question(
                UUID.fromString("48bed050-8a5d-4b3d-b0cf-77a897fca175"),
                "Help bot",
                "You can also manually add some questions using for form below. <i class=\"fas fa-arrow-down\"></i>",
                "/img/bot.png"
        );
        this.questions.put(question.uuid, question);
    }
}
