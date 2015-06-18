package de.pm.mindcloud.persistence.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created on 15/06/15
 * This class is responsible
 */

@Entity
public class Child extends DomainObject {

    @Id
    private int id;

    private String title;
    private List<Child> childs = new ArrayList<Child>();

    public Child(String title) {
        this.title = title;
    }

    public void addChild(Child node) {
        childs.add(node);
    }

    public List<Child> getChilds() {
        return childs;
    }

    public void setChilds(List<Child> childs) {
        this.childs = childs;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    @Override
    public String toString() {
        return "{\"title\": \"" + title + "\", \"childs\": " + Arrays.toString(childs.toArray()) + "}";
    }
}
