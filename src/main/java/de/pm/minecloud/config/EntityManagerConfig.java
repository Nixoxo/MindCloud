package de.pm.minecloud.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Created by samuel on 09.05.15.
 */
@Configuration
public class EntityManagerConfig {
    private EntityManagerFactory emf;

    @Value("${database.config}")
    private String persistenceUnit;

    @PostConstruct
    public void init() {
        emf = Persistence.createEntityManagerFactory(persistenceUnit);
    }

    @Bean
    public EntityManager entityManager() {
        return emf.createEntityManager();
    }
}