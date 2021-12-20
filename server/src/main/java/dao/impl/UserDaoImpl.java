package dao.impl;

import config.HibernateConfiguration;
import dao.UserDao;
import domain.entities.User;

import javax.persistence.EntityManager;

public class UserDaoImpl implements UserDao {

    private EntityManager entityManager;

    public UserDaoImpl() {
        this.entityManager = HibernateConfiguration.getEntityManager();
    }

    public boolean checkUserEmailExist(String email) {
        String query = "select count(email) from User e where e.email = :emailUser";

        Long count = (Long) entityManager
            .createQuery(query)
            .setParameter("emailUser", email)
            .getSingleResult();

        return count != 0;
    }

    // Register user
    public void save(User user) {
        entityManager.getTransaction().begin();
        entityManager.persist(user);
        entityManager.getTransaction().commit();
    }

    // Login user
    public boolean login(String email, String password) {
        String query = "select count(email) from User u "+
                " where u.email = :email and u.password = :pass";

        Long count = (Long) entityManager.createQuery(query)
            .setParameter("email",email)
            .setParameter("pass", password)
            .getSingleResult();

        return !count.equals(0L);
    }
}
