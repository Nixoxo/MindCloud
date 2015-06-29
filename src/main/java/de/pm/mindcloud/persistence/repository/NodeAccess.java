package de.pm.mindcloud.persistence.repository;

import de.pm.mindcloud.persistence.domain.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created on 18/06/15
 * This class is responsible
 */

@Component
public class NodeAccess {
    @Autowired
    private DatabaseService database;

    public Node find(String id) {
        return database.find(Node.class, id);
    }

    public void save(Node node) {
        if (node.getId() == null) {
            database.insert(node);
        } else {
            database.update(node);
        }
    }

    public void delete(Node node) {
        database.delete(node);
    }

}
