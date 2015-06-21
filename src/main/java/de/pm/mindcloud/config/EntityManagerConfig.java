package de.pm.mindcloud.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by samuel on 09.05.15.
 */
@Configuration
public class EntityManagerConfig {
    private EntityManagerFactory emf;

    @Value("${database.config}")
    private String persistenceUnit;

    @Value("${database.host}")
    private String host;

    @Value("${database.port}")
    private String port;

    @Value("${database.database}")
    private String database;

    @Value("${database.username}")
    private String username;

    @Value("${database.password}")
    private String password;


    @PostConstruct
    public void init() {
        Map<String, String> props = new HashMap<>();
        props.put("hibernate.ogm.datastore.provider", "MONGODB");
        props.put("hibernate.ogm.datastore.create_database", "true");
        props.put("hibernate.ogm.datastore.host", host);
        props.put("hibernate.ogm.datastore.port", port);
        props.put("hibernate.ogm.datastore.database", database);
        props.put("hibernate.ogm.datastore.username", username);
        props.put("hibernate.ogm.datastore.password", password);
        emf = Persistence.createEntityManagerFactory(persistenceUnit, props);
    }

    @Bean
    public EntityManager entityManager() {
        return emf.createEntityManager();
    }
}