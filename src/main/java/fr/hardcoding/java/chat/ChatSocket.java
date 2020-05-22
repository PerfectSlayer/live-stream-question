package fr.hardcoding.java.chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

import static java.util.logging.Level.WARNING;

/**
 * This class a web socket to send last {@link ChatMessage}.
 *
 * @author Bruce BUJON (bruce.bujon(at)gmail(dot)com)
 */
@ServerEndpoint("/chat")
@ApplicationScoped
public class ChatSocket {
    private static final Logger LOGGER = Logger.getLogger(ChatSocket.class.getName());
    private final List<Session> sessions = new CopyOnWriteArrayList<>();
    private final ObjectMapper mapper = new ObjectMapper();

    @OnOpen
    public void onOpen(Session session) {
        this.sessions.add(session);
    }

    @OnClose
    public void onClose(Session session) {
        this.sessions.remove(session);
    }

    /**
     * Send chat message to connected clients.
     *
     * @param message The message to send.
     */
    public void send(ChatMessage message) {
        try {
            String object = this.mapper.writeValueAsString(message);
            this.sessions.forEach(session -> session.getAsyncRemote().sendObject(object, result -> {
                if (result.getException() != null) {
                    LOGGER.warning("Unable to send message: " + result.getException());
                }
            }));
        } catch (JsonProcessingException e) {
            LOGGER.log(WARNING, "Failed to encode message "+message+".", e);
        }
    }
}
