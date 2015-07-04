package de.pm.mindcloud.web.domain.response;

import de.pm.mindcloud.persistence.domain.Mindmap;

/**
 * Created by samuel on 04.07.15.
 */
public class MindmapResponse {

    private Mindmap mindmap;

    private boolean isLocked;

    public MindmapResponse() {
    }

    public MindmapResponse(Mindmap mindmap, boolean isLocked) {
        this.mindmap = mindmap;
        this.isLocked = isLocked;
    }

    public Mindmap getMindmap() {
        return mindmap;
    }

    public void setMindmap(Mindmap mindmap) {
        this.mindmap = mindmap;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setIsLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }
}