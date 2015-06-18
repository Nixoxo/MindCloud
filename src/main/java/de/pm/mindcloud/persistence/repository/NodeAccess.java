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
    private Database database;

    public Node find(int id) {
        return database.find(Node.class, id);
    }
    public void save(Node node) {
        if (node.getId() == 0) {
            database.insert(node);
        } else {
            database.update(node);
        }
    }

    public void delete(Node node) {

        if (node.getParent() != null) {
            Node parent = node.getParent();
            parent.getNodes().remove(node);
            database.update(parent);
        }

        database.delete(node);
    }

}
