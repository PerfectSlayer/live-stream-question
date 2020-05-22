package fr.hardcoding.java;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    public QuestionModel() {
        this.questions = new HashMap<>();
        addSampleData();
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
        return this.questions.remove(uuid) != null;
    }

    private void addSampleData() {
        Question question = new Question(
                UUID.randomUUID(),
                "Bruce",
                "Est-ce que ça marche ?",
                "https://avatars3.githubusercontent.com/u/1766222?s=460&u=1567973b4b7166af57d0c534a51ad6b5922aa0e9&v=4"
        );
        this.questions.put(question.uuid, question);
        question = new Question(
                UUID.randomUUID(),
                "Charles",
                "Juste une question avec un texte assez long pour tester la taille de box parce qu'il y a de très grande chance que ce cas existe.",
                "/img/question.png"
        );
        this.questions.put(question.uuid, question);
    }
}
