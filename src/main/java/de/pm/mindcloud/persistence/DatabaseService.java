package de.pm.mindcloud.persistence;

import javax.persistence.Query;
import java.util.List;

public interface DatabaseService {
    void update(Object entity);

    void insert(Object entity);

    void delete(Object entity);

    <T> T find(Class<T> clazz, Object id);

    <T> List<T> findWithQuery(String query, Class<T> clazz);

    Query createQuery(String query, Class clazz);

    <T> List<T> findWithQuery(Query query);
}