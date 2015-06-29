package de.pm.mindcloud.persistence.repository;

import de.pm.mindcloud.persistence.domain.MindMap;
import de.pm.mindcloud.persistence.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.Query;
import java.util.List;

/**
 * Created on 18/06/15
 * This class is responsible
 */

@Component
public class UserAccess {

    @Autowired
    private DatabaseService database;

    public User find(String id) {
        return database.find(User.class, id);
    }

    public boolean usernameExists(String username) {
        Query query = database.createQuery(User.NAME, User.class);
        query.setParameter(User.USERNAME, username.toLowerCase());
        List<User> users = database.findWithQuery(query);
        return !users.isEmpty();
    }

    public User findByName(String username) {
        Query query = database.createQuery(User.NAME, User.class);
        query.setParameter(User.USERNAME, username.toLowerCase());
        List<User> users = database.findWithQuery(query);
        if (users.size() == 1) {
            return users.get(0);
        }
        return null;
    }

    public void save(User user) {
        if (user.getId() == null) {
            database.insert(user);
        } else {
            database.update(user);
        }
    }

    public void delete(MindMap mindMap) {
        database.delete(mindMap);
    }

}
