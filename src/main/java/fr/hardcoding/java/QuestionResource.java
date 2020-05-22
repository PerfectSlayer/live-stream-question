package fr.hardcoding.java;


import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;

/**
 * {@link Question} REST endpoint.
 *
 * @author Bruce BUJON (bruce.bujon(at)gmail(dot)com)
 */
@ApplicationScoped
@Path("/questions")
@Produces(APPLICATION_JSON)
public class QuestionResource {

    private final Map<UUID, Question> questions;

    public QuestionResource() {
        this.questions = new HashMap<>();
        addSampleData();
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

    @GET
    public Collection<Question> hello() {
        return this.questions.values();
    }

    @POST
    @Consumes(APPLICATION_JSON)
    public Question addQuestion(Question data) {
        Question question = new Question(
                UUID.randomUUID(),
                data.userName,
                data.text,
                "/img/question.png"
        );
        this.questions.put(question.uuid, question);
        return question;
    }

    @DELETE
    @Path("/{id}")
    public Response removeQuestion(@PathParam(value = "id") String id) {
        UUID uuid = UUID.fromString(id);
        if (this.questions.remove(uuid) != null) {
            return Response.ok().build();
        } else {
            return Response.status(NOT_FOUND).build();
        }
    }
}
