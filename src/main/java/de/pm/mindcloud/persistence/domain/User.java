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

    private String displayedName;

    private String password;

    @OneToMany
    private List<Mindmap> mindmaps;

    @OneToMany
    private List<Mindmap> sharedMindmaps;

    public User() {
        mindmaps = new ArrayList<>();
        sharedMindmaps = new ArrayList<>();
    }

    public User(String name, String password) {
        this();
        this.name = name.toLowerCase();
        this.displayedName = name;
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
        this.name = name.toLowerCase();
        if (displayedName == null || displayedName.length() == 0) {
            displayedName = name;
        }
    }

    public String getDisplayedName() {
        if (displayedName == null || displayedName.length() == 0) {
            return name;
        }
        return displayedName;
    }

    public void setDisplayedName(String displayedName) {
        this.displayedName = displayedName;
        if (displayedName == null || displayedName.length() == 0) {
            this.displayedName = name;
        }
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

    public void setSharedMindmaps(List<Mindmap> mindmaps) {
        mindmaps.forEach(this::putSharedMindmap);
    }

    public List<Mindmap> getSharedMindmaps() {
        Collections.sort(sharedMindmaps, (mindmap0, mindmap1) -> Collator.getInstance().compare(mindmap0.getName(), mindmap1.getName()));
        return sharedMindmaps;
    }

    public void putSharedMindmap(Mindmap mindmap) {
        if (mindmap.getId() == null) {
            return;
        }
        removeSharedMindmap(mindmap.getId());
        sharedMindmaps.add(mindmap);
    }

    public Mindmap removeSharedMindmap(String id) {
        Mindmap mindmap = getMindmap(id);
        sharedMindmaps.remove(mindmap);
        return mindmap;
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
        for (Mindmap mindmap : sharedMindmaps) {
            if (mindmap.getId().equals(id)) {
                return mindmap;
            }
        }
        return null;
    }

    public boolean isMindmapLocked(String mindmapId) {
        return sharedMindmaps.contains(getMindmap(mindmapId));
    }
}