package dao.impl;

import dao.BroadcastToGroupDao;
import domain.client.dto.BroadcastToGroupDto;

import javax.persistence.EntityManager;


public class BroadcastToGroupDaoImpl implements BroadcastToGroupDao {
    // TODO: Implement
    private static final String QUERY_PARAM_GROUP_ID = ":groupID";
    private static final String QUERY_PARAM_USER_ID = ":userID";
    private static final String QUERY_STRING =
            "INSERT INTO GroupNotification(id, group)" +
            "SELECT Group AS group, " +
                    "(SELECT User as user WHERE user.id=" + QUERY_PARAM_USER_ID + " ) AS user, " +
                    "new Notification() AS notification" +
                    "new NotificationId(user, notification) AS id," +
            "WHERE group.id=" + QUERY_PARAM_GROUP_ID;

    private final EntityManager entityManager;

    public BroadcastToGroupDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void broadcast(BroadcastToGroupDto message) {
        entityManager.getTransaction().begin();
        entityManager.createQuery( QUERY_STRING ).executeUpdate();
        entityManager.getTransaction().commit();
    }
}
