package fr.hardcoding.java.chat;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * {@link ChatMessage} REST endpoint.
 *
 * @author Bruce BUJON (bruce.bujon(at)gmail(dot)com)
 */
@Path("/chat")
@Produces(APPLICATION_JSON)
public class ChatResource {

    @Inject
    ChatModel model;

    @GET
    public List<ChatMessage> list() {
        return this.model.list();
    }
}
