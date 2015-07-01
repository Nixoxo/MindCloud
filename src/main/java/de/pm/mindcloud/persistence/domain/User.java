package de.pm.mindcloud.persistence.domain;

import javax.persistence.*;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.apache.commons.lang.builder.ToStringBuilder.reflectionToString;

/**
 * Created by samuel on 29.06.15.
 */
@Entity
@NamedQueries({
        @NamedQuery(name = User.NAME, query = "from User u where u.name like :username")
})
public class User {
    public static final String NAME = "User.NAME";
    public static final String USERNAME = "username";

    @Id
    private String id;

    private String name;

    private String password;

    @OneToMany
    private List<Mindmap> mindmaps;

    public User() {
        mindmaps = new ArrayList<>();
    }

    public User(String name, String password) {
        this();
        this.name = name.toLowerCase();
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setMindmaps(List<Mindmap> mindmaps) {
        mindmaps.forEach(this::putMindmap);
    }

    public List<Mindmap> getMindmaps() {
        Collections.sort(mindmaps, (mindmap0, mindmap1) -> Collator.getInstance().compare(mindmap0.getName(), mindmap1.getName()));
        return mindmaps;
    }

    public void putMindmap(Mindmap mindmap) {
        if (mindmap.getId() == null) {
            return;
        }
        removeMindmap(mindmap.getId());
        mindmaps.add(mindmap);
    }

    public Mindmap removeMindmap(String id) {
        Mindmap mindmap = getMindmap(id);
        mindmaps.remove(mindmap);
        return mindmap;
    }

    @Override
    public String toString() {
        return reflectionToString(this);
    }

    public Mindmap getMindmap(String id) {
        for (Mindmap mindmap : mindmaps) {
            if (mindmap.getId().equals(id)) {
                return mindmap;
            }
        }
        return null;
    }
}