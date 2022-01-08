package dao.impl;

import config.HibernateConfiguration;
import dao.MessageDao;
import domain.client.dto.MessageDto;
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
                "    n.id, " +
                "    n.content, " +
                "    n.messageType, " +
                "    n.sendDate, " +
                "    f.filePath," +
                "    f.fileName," +
                "    f.fileType," +
                "    n.sender.id," +
                "    n.sender.username," +
                "    n.sender.id = :userId," +
                "    n.group.id) " +
                " FROM Notification n " +
                " LEFT OUTER JOIN n.file f" +
                " WHERE (n.group.id = :groupId" +
                "   OR EXISTS " +
                "      (SELECT 1" +
                "        FROM Group g" +
                "       WHERE g.id = :groupId" +
                "        AND g.parent.id = n.group.id" +
                "        AND n.sendDate <= g.creationDate))" +
                "  AND n.sendDate >= " +
                "       (SELECT ug.joinDate" +
                "         FROM UserGroup ug" +
                "        WHERE ug.id.user.id = :userId" +
                "         AND ug.id.group.id = :groupId)" +
                " ORDER BY n.sendDate";

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
    public Notification getNotificationById(Long notificationId) {
        return entityManager.find(Notification.class, notificationId);
    }
}
