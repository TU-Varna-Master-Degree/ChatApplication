package dao.impl;

import dao.UserDao;
import domain.entities.User;

import javax.persistence.EntityManager;

public class UserDaoImpl implements UserDao {

    private EntityManager entityManager;

    public UserDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    // Register user
    public void save(User user) {

         String query = "select count(email) from User e where e.email=: emailUser";

         Long count = (Long) entityManager.createQuery( query ).
                 setParameter("emailUser",user.getEmail()).getSingleResult();
           if (( ( count.equals( 0L ) ) ? true : false )){
        entityManager.getTransaction().begin();
        entityManager.persist(user);
        entityManager.getTransaction().commit();}
    }
}
