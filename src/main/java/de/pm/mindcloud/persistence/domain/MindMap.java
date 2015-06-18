package de.pm.mindcloud.persistence.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created on 15/06/15
 * This class is responsible
 */
@Entity
public class MindMap {

    private String title;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Node> nodes = new ArrayList<>();

    public MindMap(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    @Override
    public String toString() {
        return "{\"title\": \"" + title + "\", \"nodes\": " + Arrays.toString(nodes.toArray()) + "}";
    }
}
