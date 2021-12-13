package dao.impl;

import dao.AddToGroupDao;
import domain.entities.Group;
import domain.entities.User;
import domain.entities.UserGroup;
import domain.entities.UserGroupId;

import javax.persistence.EntityManager;
import javax.persistence.Query;

// добавя потребител към група по id на потребителя и групата

public class AddToGroupDaoImpl implements AddToGroupDao {
    private EntityManager entityManager;

    public AddToGroupDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    public void AddToGroup(long userId,long groupId) {

        UserGroupId userGroupId = new UserGroupId();
        UserGroup userGroup = new UserGroup();
        long counts;

        String check = "Select count (ug) from UserGroup ug " +
                "where ug.id.user.id=:user and " +
                "ug.id.group.id=:group";
        Query queryCheck = entityManager.createQuery(check).
                setParameter("user", userId).
                setParameter("group", groupId);
        counts = (long) queryCheck.getSingleResult();

        if (counts == 0) {

            userGroupId.setUser((User) entityManager.createQuery("Select" +
                            " user from User user where user.id=:userId").
                    setParameter("userId", userId).
                    getSingleResult());
            userGroupId.setGroup((Group) entityManager.
                    createQuery("Select group from Group group where group.id=:groupI").
                    setParameter("groupI", groupId).
                    getSingleResult());

            userGroup.setId(userGroupId);
            userGroup.setJoinDate(java.time.LocalDateTime.now());

            entityManager.getTransaction().begin();
            entityManager.persist(userGroup);
            entityManager.getTransaction().commit();
        } else System.out.println("\n Вече съществува такъв потребител в тази група\n");
    }

}


