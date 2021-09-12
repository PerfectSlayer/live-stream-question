package fr.hardcoding.java.question;


import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;

/**
 * {@link Question} REST endpoint.
 *
 * @author Bruce BUJON (bruce.bujon(at)gmail(dot)com)
 */
@Path("/questions")
@Produces(APPLICATION_JSON)
public class QuestionResource {

    @Inject
    QuestionModel model;

    @GET
    public List<Question> listQuestions() {
        return this.model.list();
    }

    @POST
    @Consumes(APPLICATION_JSON)
    public Question addQuestion(Question data) {
        Question question = new Question(
                UUID.randomUUID(),
                data.userName,
                data.text,
                "/img/question.png",
                false
        );
        this.model.add(question);
        return question;
    }

    @PATCH
    @Path("/{id}/answered")
    public Response toggleAnswered(@PathParam(value = "id") String id) {
        UUID uuid = UUID.fromString(id);
        if (this.model.toggleAnswered(uuid)) {
            return Response.ok().build();
        } else {
            return Response.status(NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response removeQuestion(@PathParam(value = "id") String id) {
        UUID uuid = UUID.fromString(id);
        if (this.model.remove(uuid)) {
            return Response.ok().build();
        } else {
            return Response.status(NOT_FOUND).build();
        }
    }
}
