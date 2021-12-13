package dao.impl;

import dao.CreateGroupDao;
import domain.entities.Group;
import domain.entities.User;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class CreateGroupDaoImpl implements CreateGroupDao {
    private final EntityManager entityManager;
    private Object LocalDateTime;

    public CreateGroupDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public boolean CreateGroup ( long userId,String nameGr) {
        Group newGroup;
         long counts;
       Query query = entityManager.createQuery("select COUNT (name) from " +
                        "Group gr where gr.name =: newName").
                setParameter("newName", nameGr);
         counts = (Long) query.getSingleResult();
        System.out.println(counts);

     if (counts == 0) {
            String q = "Select users " +
                    "from User users where users.id=:id";
            Query qu = entityManager.createQuery(q);
            qu.setParameter("id", userId);
            User userGr = (User) qu.getSingleResult();

            newGroup = new Group(java.time.LocalDateTime.now(), nameGr, userGr);

            entityManager.getTransaction().begin();
            entityManager.persist(newGroup);
            entityManager.getTransaction().commit();
            return true;
        }
        else {System.out.println("Вече има такава група");
            return false;
    }

}}
