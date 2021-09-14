package fr.hardcoding.java.chat;

import fr.hardcoding.java.question.Question;
import fr.hardcoding.java.question.QuestionModel;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;

/**
 * {@link ChatMessage} REST endpoint.
 *
 * @author Bruce BUJON (bruce.bujon(at)gmail(dot)com)
 */
@Path("/chat")
@Produces(APPLICATION_JSON)
public class ChatResource {

    @Inject
    ChatModel chatModel;
    @Inject
    QuestionModel questionModel;


    @GET
    public List<ChatMessage> list() {
        return this.chatModel.list();
    }

    @POST
    @Path("/{id}")
    public Response pin(@PathParam(value = "id") String chatMessageId) {
        UUID uuid = UUID.fromString(chatMessageId);
        Optional<ChatMessage> chatMessage = this.chatModel.getFromId(uuid);
        if (chatMessage.isPresent()) {
            ChatMessage message = chatMessage.get();
            Question question = new Question(uuid, message.userName, message.text, message.profileUrl, false);
            this.questionModel.add(question);
            return Response.ok().build();
        } else {
            return Response.status(NOT_FOUND).build();
        }
    }
}
