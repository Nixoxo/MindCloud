package de.pm.mindcloud.persistence.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created on 15/06/15
 * This class is responsible
 */
@Entity
public class MindMap {

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private String id;

    private String name;

    @OneToMany
    private List<Node> nodes = new ArrayList<>();

    // TODO add edges list

    public MindMap() {
    }

    public MindMap(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "{\"name\": \"" + name + "\", \"nodes\": " + Arrays.toString(nodes.toArray()) + "}";
    }
}
