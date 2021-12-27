package dao.impl;

import config.HibernateConfiguration;
import dao.UserDao;
import domain.entities.User;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

public class UserDaoImpl implements UserDao {

    private final EntityManager entityManager;

    public UserDaoImpl() {
        this.entityManager = HibernateConfiguration.getEntityManager();
    }

    public boolean checkUsernameExist(String username) {
        String query = "select count(username) from User e where e.username = :username";

        Long count = (Long) entityManager
                .createQuery(query)
                .setParameter("username", username)
                .getSingleResult();

        return count != 0;
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
    public Long login(String username, String password) {
        String hql = "select u.id from User u "+
                " where u.username = :username and u.password = :pass";

        Query query = entityManager.createQuery(hql);
        query.setParameter("username",username);
        query.setParameter("pass", password);

        try {
            return (Long) query.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public User getById(Long userId) {
        return entityManager.find(User.class, userId);
    }
}
