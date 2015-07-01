package de.pm.mindcloud.persistence.repository;

import de.pm.mindcloud.persistence.domain.Mindmap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Created on 18/06/15
 * This class is responsible
 */

@Component
public class MindmapAccess {

    @Autowired
    private DatabaseService database;

    public Mindmap find(String id) {
        return database.find(Mindmap.class, id);
    }

    public void save(Mindmap mindmap) {
        if (mindmap.getId() == null) {
            mindmap.setId(UUID.randomUUID().toString());
            database.insert(mindmap);
        } else {
            Mindmap m = find(mindmap.getId());
            if (m != null) {
                delete(m);
            }
            database.insert(mindmap);
        }
    }

    public void delete(Mindmap mindmap) {
        mindmap.getNodes().forEach(database::delete);
        mindmap.getEdges().forEach(database::delete);
        database.delete(mindmap);
    }
}
