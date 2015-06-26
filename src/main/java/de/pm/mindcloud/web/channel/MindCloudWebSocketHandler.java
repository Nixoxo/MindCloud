package de.pm.mindcloud.web.channel;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.pm.mindcloud.web.domain.request.MindmapRequest;
import org.apache.log4j.Logger;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * Created by samuel on 23.06.15.
 */
public class MindCloudWebSocketHandler extends TextWebSocketHandler {
    private final Logger logger = Logger.getLogger(MindCloudWebSocketHandler.class);

    private final ObjectMapper objectMapper;

    public MindCloudWebSocketHandler() {
        objectMapper = new ObjectMapper();
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        logger.info(session.getId() + " loaded");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
        JsonNode event = objectMapper.readTree(message.getPayload());
        switch (event.get("action").asText()) {
            case "getMindmapList":
                // TODO
                break;
            case "getMindmap":
                MindmapRequest request = objectMapper.treeToValue(event.get("data"), MindmapRequest.class);
                // TODO
                break;
        }
    }
}