package de.pm.mindcloud.web.controller;

import de.pm.mindcloud.persistence.domain.Mindmap;
import de.pm.mindcloud.persistence.domain.User;
import de.pm.mindcloud.persistence.repository.MindmapAccess;
import de.pm.mindcloud.persistence.repository.UserAccess;
import de.pm.mindcloud.web.domain.request.MindmapRequest;
import de.pm.mindcloud.web.domain.request.SearchRequest;
import de.pm.mindcloud.web.domain.response.MindmapResponse;
import de.pm.mindcloud.web.domain.response.UserMindmapDataResponse;
import de.pm.mindcloud.web.domain.response.MindmapDataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by samuel on 23.06.15.
 */
@Controller
public class MindCloudController {
    @Autowired
    private SessionController sessionController;

    @Autowired
    private MindmapAccess mindmapAccess;

    @Autowired
    private UserAccess userAccess;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/getMindmapList")
    @SendTo("/setMindmapList")
    public List<MindmapDataResponse> getMindmapList(SimpMessageHeaderAccessor headerAccessor) throws Exception {
        User user = getUserFromRequest(headerAccessor);
        if (user != null) {
            return getMindmapResponses(user.getMindmaps());
        }
        return null;
    }

    @MessageMapping("/getSharedMindmapList")
    @SendTo("/setSharedMindmapList")
    public List<MindmapDataResponse> getSharedMindmapList(SimpMessageHeaderAccessor headerAccessor) throws Exception {
        User user = getUserFromRequest(headerAccessor);
        if (user != null) {
            return getMindmapResponses(user.getSharedMindmaps());
        }
        return null;
    }

    @MessageMapping("/searchMindmapList")
    @SendTo("/setSearchResult")
    public List<MindmapDataResponse> searchMindmapList(SimpMessageHeaderAccessor headerAccessor, SearchRequest request) throws Exception {
        User user = getUserFromRequest(headerAccessor);
        if (user != null) {
            List<Mindmap> searchResult = searchMindmaps(request.getSearch(), user.getMindmaps(), user.getSharedMindmaps());
            return getMindmapResponses(searchResult);
        }
        return null;
    }

    private List<Mindmap> searchMindmaps(String search, List<Mindmap>... mindmaps) {
        List<Mindmap> result = new ArrayList<>();
        for (List<Mindmap> list : mindmaps) {
            list.stream().filter(m -> m.contains(search)).collect(Collectors.toList()).forEach(result::add);
        }
        return result;
    }

    private List<MindmapDataResponse> getMindmapResponses(List<Mindmap> mindmaps) {
        List<MindmapDataResponse> mindmapList = new ArrayList<>();
        for (Mindmap mindmap : mindmaps) {
            MindmapDataResponse m = new MindmapDataResponse();
            m.setId(mindmap.getId());
            m.setName(mindmap.getName());
            mindmapList.add(m);
        }
        return mindmapList;
    }

    @MessageMapping("/getMindmap")
    @SendTo("/setMindmap")
    public MindmapResponse getMindmap(SimpMessageHeaderAccessor headerAccessor, MindmapRequest request) throws Exception {
        User user = getUserFromRequest(headerAccessor);
        if (user != null) {
            Mindmap mindmap = user.getMindmap(request.getId());
            if (mindmap != null) {
                return new MindmapResponse(mindmap, user.isMindmapLocked(mindmap.getId()));
            }
        }
        return null;
    }

    @MessageMapping("/saveMindmap")
    @SendTo("/setMindmap")
    public MindmapResponse saveMindmap(SimpMessageHeaderAccessor headerAccessor, Mindmap mindmap) {
        User user = getUserFromRequest(headerAccessor);
        if (user != null && !user.isMindmapLocked(mindmap.getId())) {
            mindmapAccess.save(mindmap);
            user.putMindmap(mindmap);
            userAccess.save(user);
            return new MindmapResponse(mindmap, user.isMindmapLocked(mindmap.getId()));
        }
        return null;
    }

    @MessageMapping("/deleteMindmap")
    @SendTo("/deleteMindmapSuccess")
    public boolean deleteMindmap(SimpMessageHeaderAccessor headerAccessor, MindmapRequest request) {
        User user = getUserFromRequest(headerAccessor);
        if (user != null) {
            Mindmap mindmap = user.removeMindmap(request.getId());
            if (mindmap != null) {
                mindmapAccess.delete(mindmap);
                userAccess.save(user);
                return true;
            }
        }
        return false;
    }

    @MessageMapping("/getUserMindmapData")
    @SendTo("/setUserMindmapData")
    public UserMindmapDataResponse getUserMindmapData(SimpMessageHeaderAccessor headerAccessor) {
        User user = getUserFromRequest(headerAccessor);
        if (user != null) {
            int mindmapsCount = user.getMindmaps().size();
            int nodesCount = 0;
            for (Mindmap mindmap : user.getMindmaps()) {
                nodesCount += mindmap.getNodes().size();
            }
            return new UserMindmapDataResponse(mindmapsCount, nodesCount);
        }
        return null;
    }

    private User getUserFromRequest(SimpMessageHeaderAccessor headerAccessor) {
        String httpSessionId = (String) headerAccessor.getSessionAttributes().get("HTTPSESSIONID");
        User user = sessionController.getUserBySessionId(httpSessionId);
        if (user == null) {
            send("/logout", true);
        }
        return user;
    }

    public void send(String destination, Object message) {
        messagingTemplate.convertAndSend(destination, message);
    }
}