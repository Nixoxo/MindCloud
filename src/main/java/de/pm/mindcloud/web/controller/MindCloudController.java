package de.pm.mindcloud.web.controller;

import de.pm.mindcloud.persistence.domain.Mindmap;
import de.pm.mindcloud.persistence.domain.User;
import de.pm.mindcloud.persistence.repository.MindmapAccess;
import de.pm.mindcloud.persistence.repository.UserAccess;
import de.pm.mindcloud.web.controller.SessionController;
import de.pm.mindcloud.web.domain.request.MindmapRequest;
import de.pm.mindcloud.web.domain.request.SearchRequest;
import de.pm.mindcloud.web.domain.response.MindmapDataResponse;
import de.pm.mindcloud.web.domain.response.MindmapResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

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

    @MessageMapping("/getMindmapList")
    @SendTo("/setMindmapList")
    public List<MindmapResponse> getMindmapList(SimpMessageHeaderAccessor headerAccessor) throws Exception {
        User user = getUserFromRequest(headerAccessor);
        return getMindmapResponses(user.getMindmaps());
    }

    @MessageMapping("/searchMindmapList")
    @SendTo("/setSearchResult")
    public List<MindmapResponse> searchMindmapList(SimpMessageHeaderAccessor headerAccessor, SearchRequest request) throws Exception {
        User user = getUserFromRequest(headerAccessor);
        List<Mindmap> searchResult = searchMindmaps(user.getMindmaps(), request.getSearch());
        return getMindmapResponses(searchResult);
    }

    private List<Mindmap> searchMindmaps(List<Mindmap> mindmaps, String search) {
        return mindmaps.stream().filter(m -> m.contains(search)).collect(Collectors.toList());
    }

    private List<MindmapResponse> getMindmapResponses(List<Mindmap> mindmaps) {
        List<MindmapResponse> mindmapList = new ArrayList<>();
        for (Mindmap mindmap : mindmaps) {
            MindmapResponse m = new MindmapResponse();
            m.setId(mindmap.getId());
            m.setName(mindmap.getName());
            mindmapList.add(m);
        }
        return mindmapList;
    }

    @MessageMapping("/getMindmap")
    @SendTo("/setMindmap")
    public Mindmap getMindmap(SimpMessageHeaderAccessor headerAccessor, MindmapRequest request) throws Exception {
        User user = getUserFromRequest(headerAccessor);
        for (Mindmap mindmap : user.getMindmaps()) {
            if (mindmap.getId().equals(request.getId())) {
                return mindmap;
            }
        }
        return null;
    }

    @MessageMapping("/saveMindmap")
    @SendTo("/setMindmap")
    public Mindmap saveMindmap(SimpMessageHeaderAccessor headerAccessor, Mindmap mindmap) {
        User user = getUserFromRequest(headerAccessor);
        mindmapAccess.save(mindmap);
        user.putMindmap(mindmap);
        userAccess.save(user);
        return mindmap;
    }

    @MessageMapping("/deleteMindmap")
    @SendTo("/deleteMindmapSuccess")
    public boolean deleteMindmap(SimpMessageHeaderAccessor headerAccessor, MindmapRequest request) {
        User user = getUserFromRequest(headerAccessor);
        Mindmap mindmap = user.removeMindmap(request.getId());
        if (mindmap != null) {
            mindmapAccess.delete(mindmap);
            userAccess.save(user);
            return true;
        }
        return false;
    }

    @MessageMapping("/getUserMindmapData")
    @SendTo("/setUserMindmapData")
    public MindmapDataResponse getUserMindmapData(SimpMessageHeaderAccessor headerAccessor) {
        User user = getUserFromRequest(headerAccessor);
        int mindmapsCount = user.getMindmaps().size();
        int nodesCount = 0;
        for (Mindmap mindmap : user.getMindmaps()) {
            nodesCount += mindmap.getNodes().size();
        }
        return new MindmapDataResponse(mindmapsCount, nodesCount);
    }

    private User getUserFromRequest(SimpMessageHeaderAccessor headerAccessor) {
        String httpSessionId = (String) headerAccessor.getSessionAttributes().get("HTTPSESSIONID");
        return sessionController.getUserBySessionId(httpSessionId);
    }
}