package de.pm.mindcloud.persistence.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created on 15/06/15
 * This class is responsible
 */
public class MindMap {

    private String title;

    //private List<Child> nodes = new ArrayList<Child>();

    public MindMap(String title) {
        this.title = title;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

//    @Override
//    public String toString() {
//        return "{\"title\": \"" + title + "\", \"nodes\": " + Arrays.toString(nodes.toArray()) + "}";
//    }
}
