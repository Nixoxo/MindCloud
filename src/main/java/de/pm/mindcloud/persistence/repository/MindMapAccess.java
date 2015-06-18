package de.pm.mindcloud.persistence.repository;

import de.pm.mindcloud.persistence.domain.MindMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created on 18/06/15
 * This class is responsible
 */

@Component
public class MindMapAccess {

    @Autowired
    private DatabaseService database;

    public MindMap find(int id) {
        return database.find(MindMap.class, id);
    }

    public void save(MindMap mindMap) {
        database.save(mindMap);
    }

    public void delete(MindMap mindMap) {
        database.delete(mindMap);
    }

}
