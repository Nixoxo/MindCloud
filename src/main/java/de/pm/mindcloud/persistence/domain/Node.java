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
public class Node {

    @Id
    private String id;

    private String title;

    public Node() {
    }

    public Node(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "{\"title\": \"" + title + "\"}";
    }
}
