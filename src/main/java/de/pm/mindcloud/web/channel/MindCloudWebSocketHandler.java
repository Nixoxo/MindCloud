package de.pm.mindcloud.web.channel;

import org.apache.log4j.Logger;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * Created by samuel on 23.06.15.
 */
public class MindCloudWebSocketHandler extends TextWebSocketHandler {

    private Logger logger = Logger.getLogger(MindCloudWebSocketHandler.class);

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        logger.info(session.getId() + " loaded");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
        logger.info(message.getPayload());
    }
}