package de.pm.mindcloud.persistence.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created on 15/06/15
 * This class is responsible
 */

@Entity
public class Node extends DomainObject {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String title;

    @OneToOne
    private Node parent;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Node> nodes = new ArrayList<>();

    public Node() {
    }

    public Node(String title) {
        this.title = title;
    }

    public void addNode(Node node) {
        node.setParent(this);
        nodes.add(node);
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "{\"title\": \"" + title + "\", \"nodes\": " + Arrays.toString(nodes.toArray()) + "}";
    }
}
