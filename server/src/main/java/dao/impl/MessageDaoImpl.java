package dao.impl;

import config.HibernateConfiguration;
import dao.MessageDao;
import domain.client.dto.MessageDto;
import domain.entities.ChatFile;
import domain.entities.GroupNotification;
import domain.entities.Notification;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

public class MessageDaoImpl implements MessageDao {

    private final EntityManager entityManager;

    public MessageDaoImpl() {
        this.entityManager = HibernateConfiguration.getEntityManager();
    }

    @Override
    public List<MessageDto> getGroupMessages(Long userId, Long groupId) {
        String sql = "SELECT new domain.client.dto.MessageDto(" +
                "    gn.id.notification.id, " +
                "    gn.id.notification.content, " +
                "    gn.id.notification.messageType, " +
                "    gn.id.notification.sendDate, " +
                "    f.filePath," +
                "    f.fileName," +
                "    f.fileType) " +
                " FROM GroupNotification gn " +
                " LEFT OUTER JOIN gn.id.notification.file f" +
                " WHERE (gn.group.id = :groupId" +
                "   OR EXISTS " +
                "      (SELECT 1" +
                "        FROM Group g" +
                "       WHERE g.id = :groupId" +
                "        AND g.parent.id = gn.group.id" +
                "        AND gn.id.notification.sendDate <= g.creationDate))" +
                "  AND gn.id.notification.sendDate >= " +
                "       (SELECT ug.joinDate" +
                "         FROM UserGroup ug" +
                "        WHERE ug.id.user.id = :userId" +
                "         AND ug.id.group.id = :groupId)" +
                " ORDER BY gn.id.notification.sendDate DESC";

        Query query = entityManager.createQuery(sql);
        query.setParameter("userId", userId);
        query.setParameter("groupId", groupId);
        return query.getResultList();
    }

    @Override
    public void saveNotification(Notification notification) {
        entityManager.getTransaction().begin();
        entityManager.persist(notification);
        entityManager.getTransaction().commit();
    }

    @Override
    public void saveGroupNotification(GroupNotification groupNotification) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(groupNotification.getId().getNotification());
            entityManager.persist(groupNotification);
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            entityManager.getTransaction().rollback();
        }
    }

    @Override
    public GroupNotification getGroupNotificationById(Long senderId, Long notificationId) {
        String sql = "SELECT gn " +
                "FROM GroupNotification gn " +
                "WHERE gn.id.notification.id = :notificationId " +
                " AND gn.id.sender.id = :senderId";

        Query query = entityManager.createQuery(sql);
        query.setParameter("notificationId", notificationId);
        query.setParameter("senderId", senderId);

        try {
            return (GroupNotification) query.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }
}
