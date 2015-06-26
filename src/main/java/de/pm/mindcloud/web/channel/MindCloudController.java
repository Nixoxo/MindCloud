package de.pm.mindcloud.web.channel;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.pm.mindcloud.persistence.domain.MindMap;
import de.pm.mindcloud.web.domain.request.MindmapRequest;
import de.pm.mindcloud.web.domain.response.MindmapResponse;
import org.apache.log4j.Logger;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by samuel on 23.06.15.
 */
@Controller
public class MindCloudController {
    private final Logger logger = Logger.getLogger(MindCloudController.class);

    @MessageMapping("/getMindmapList")
    @SendTo("/getMindmapList")
    public List<MindmapResponse> getMindmapList() throws Exception {
        List<MindmapResponse> mindmapList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            MindmapResponse mindmap =  new MindmapResponse();
            mindmap.setId(String.valueOf(i));
            mindmap.setName("Mindmap " + (i + 1));
            mindmapList.add(mindmap);
        }
        return mindmapList;
    }

    @MessageMapping("/getMindmap")
    @SendTo("/getMindmap")
    public MindMap getMindmap(MindmapRequest request) throws Exception {
        MindMap mindmap = new MindMap("Mindmap");
        // TODO change mindmap class...

        return mindmap;
    }
}