package dao.impl;

import dao.EditMessageDao;

import javax.persistence.EntityManager;

public class EditMessageDaoImpl implements EditMessageDao {
    private static final String QUERY_PARAM_NOTIFICATION_ID = ":notification_id";
    private static final String QUERY_STRING =
            "UPDATE Notification" +
            "SET received=true" +
            "WHERE id=" + QUERY_PARAM_NOTIFICATION_ID;

    private final EntityManager manager;

    public EditMessageDaoImpl(EntityManager manager) {
        this.manager = manager;
    }

    @Override
    public void markAsReceived(Long notificationId) {
        manager.getTransaction().begin();
        manager.createQuery( QUERY_STRING )
                .setParameter( QUERY_PARAM_NOTIFICATION_ID, notificationId )
                .executeUpdate();
        manager.getTransaction().commit();
    }
}
