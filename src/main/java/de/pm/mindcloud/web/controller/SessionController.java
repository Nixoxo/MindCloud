package de.pm.mindcloud.web.controller;

import de.pm.mindcloud.persistence.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by samuel on 30.06.15.
 */
@Component
public class SessionController implements HttpSessionListener {
    public static final String USER = "user";

    private Map<String, HttpSession> sessions;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public SessionController() {
        sessions = new HashMap<>();
    }

    public HttpSession getSession(String id) {
        HttpSession session = sessions.get(id);
        return session;
    }

    public User getUserBySessionId(String id) {
        HttpSession session = getSession(id);
        if (session == null) {
            return null;
        }
        return (User) session.getAttribute(USER);
    }

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        HttpSession session = httpSessionEvent.getSession();
        sessions.put(session.getId(), session);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        HttpSession session = httpSessionEvent.getSession();
        sessions.remove(session.getId());
        send("/logout", true);
    }

    public void send(String destination, Object message) {
        messagingTemplate.convertAndSend(destination, message);
    }
}