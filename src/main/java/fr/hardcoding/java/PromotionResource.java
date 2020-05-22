package fr.hardcoding.java;

import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import java.util.Optional;
import java.util.UUID;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;

/**
 * Promoted {@link Question} REST endpoint.
 *
 * @author Bruce BUJON (bruce.bujon(at)gmail(dot)com)
 */
@Path("/promotion")
@Produces(APPLICATION_JSON)
public class PromotionResource {

    @Inject
    QuestionModel model;

    @GET
    public Response getPromotion() {
        Optional<Question> promoted = this.model.getPromoted();
        if (promoted.isPresent()) {
            return Response.ok(promoted.get()).build();
        } else {
            return Response.status(NOT_FOUND).build();
        }
    }

    @POST
    @Path("/{id}")
    public void promote(@PathParam(value = "id") String id) {
        this.model.promote(UUID.fromString(id));
    }

    @DELETE
    public void demote() {
        this.model.demote();
    }
}
