package de.pm.mindcloud.persistence.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created on 15/06/15
 * This class is responsible
 */

@Entity
public class MindmapData {

    @Id
    @GeneratedValue(generator="uuid2")
    @GenericGenerator(name="uuid2", strategy = "uuid2")
    @JsonIgnore
    private String id;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "Data", joinColumns = @JoinColumn(name = "mindmap_id"))
    @MapKeyColumn(name = "key")
    @Column(name = "value")
    private Map<String, String> data;

    public MindmapData() {
        this.data = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

    public void putData(String key, String value) {
        data.put(key, value);
    }

    public boolean contains(String search) {
        List<String> allowedKeys = Arrays.asList(new String[]{"title"});
        Map<String, String> result = data.entrySet().parallelStream().filter(e -> e.getValue().toLowerCase().contains(search.toLowerCase()) && allowedKeys.contains(e.getKey())).collect(Collectors.toMap(e -> e.getKey(), e-> e.getValue()));
        return !result.isEmpty();
    }
}