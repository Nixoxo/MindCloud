package de.pm.mindcloud.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.util.List;

@Component
public class Database implements DatabaseService {

    @Autowired
    private EntityManager entityManager;

    @Override
    public void insert(Object entity) {
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        entityManager.persist(entity);
        tx.commit();
    }

    @Override
    public void update(Object entity) {
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        entityManager.merge(entity);
        tx.commit();
    }

    @Override
    public void delete(Object entity) {
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        entityManager.remove(entity);
        tx.commit();
    }

    @Override
    public <T> T find(Class<T> clazz, Object id) {
        return entityManager.find(clazz, id);
    }

    @Override
    public <T> List<T> findWithQuery(String query, Class<T> clazz) {
        return createQuery(query, clazz).getResultList();
    }

    @Override
    public Query createQuery(String query, Class clazz) {
        return entityManager.createNamedQuery(query, clazz);
    }

    @Override
    public <T> List<T> findWithQuery(Query query) {
        return query.getResultList();
    }
}