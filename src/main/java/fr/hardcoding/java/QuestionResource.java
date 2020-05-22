package fr.hardcoding.java;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * {@link Question} REST endpoint.
 *
 * @author Bruce BUJON (bruce.bujon(at)gmail(dot)com)
 */
@Path("/questions")
public class QuestionResource {

    private final Map<UUID, Question> questions;

    public QuestionResource() {
        this.questions = new HashMap<>();
    }

    @GET
    @Produces(APPLICATION_JSON)
    public List<Question> hello() {

        List<Question> questions = new ArrayList<>();

        questions.add(new Question(
                UUID.randomUUID(),
                "Bruce",
                "Est-ce que ça marche ?",
                "https://avatars3.githubusercontent.com/u/1766222?s=460&u=1567973b4b7166af57d0c534a51ad6b5922aa0e9&v=4"
        ));

        questions.add(new Question(
                        UUID.randomUUID(),
                        "Charles",
                        "Juste une question avec un texte assez long pour tester la taille de box parce qu'il y a de très grande chance que ce cas existe.",
                        "/img/question.png"
                )
        );

        return questions;
    }


    @POST
    @Consumes(APPLICATION_JSON)
    public void addQuestion(Question question) {
        UUID uuid = UUID.randomUUID();
        question.uuid = uuid;
        question.profileUrl = "/img/question.png";
        this.questions.put(uuid, question);
    }


}