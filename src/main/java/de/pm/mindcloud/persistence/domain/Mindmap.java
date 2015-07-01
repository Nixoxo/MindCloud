package de.pm.mindcloud.persistence.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created on 15/06/15
 * This class is responsible
 */
@Entity
public class Mindmap {

    @Id
    private String id;

    private String name;

    @OneToMany(cascade = CascadeType.ALL)
    private List<MindmapData> nodes = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    private List<MindmapData> edges = new ArrayList<>();

    public Mindmap() {
    }

    public Mindmap(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MindmapData> getNodes() {
        return nodes;
    }

    public void setNodes(List<MindmapData> nodes) {
        this.nodes = nodes;
    }

    public List<MindmapData> getEdges() {
        return edges;
    }

    public void setEdges(List<MindmapData> edges) {
        this.edges = edges;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "{\"name\": \"" + name + "\", \"nodes\": " + Arrays.toString(nodes.toArray()) + ", \"edges\": " + Arrays.toString(edges.toArray()) + "}";
    }

    public boolean contains(String search) {
        if (name.toLowerCase().contains(search.toLowerCase())) {
            return true;
        }
        List<MindmapData> result = nodes.stream().filter(n -> n.contains(search)).collect(Collectors.toList());
        return !result.isEmpty();
    }
}