package config;

import org.hibernate.cfg.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class HibernateConfiguration {

    private static EntityManagerFactory entityManagerFactory;
    private static EntityManager entityManager;

    public static void init() {
        Configuration cfg = new Configuration();
        cfg.configure("hibernate.cfg.xml");
        entityManagerFactory = cfg.buildSessionFactory();
        entityManager = entityManagerFactory.createEntityManager();
    }

    public static void close() {
        entityManager.close();
        entityManagerFactory.close();
    }

    public static EntityManager getEntityManager() {
        return entityManager;
    }
}
