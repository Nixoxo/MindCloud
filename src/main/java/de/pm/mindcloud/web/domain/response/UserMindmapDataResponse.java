package de.pm.mindcloud.web.domain.response;

/**
 * Created by samuel on 03.07.15.
 */
public class UserMindmapDataResponse {

    private int mindmapsCount;
    private int nodesCount;

    public UserMindmapDataResponse() {
    }

    public UserMindmapDataResponse(int mindmapsCount, int nodesCount) {
        this.mindmapsCount = mindmapsCount;
        this.nodesCount = nodesCount;
    }

    public int getMindmapsCount() {
        return mindmapsCount;
    }

    public void setMindmapsCount(int mindmapsCount) {
        this.mindmapsCount = mindmapsCount;
    }

    public int getNodesCount() {
        return nodesCount;
    }

    public void setNodesCount(int nodesCount) {
        this.nodesCount = nodesCount;
    }
}