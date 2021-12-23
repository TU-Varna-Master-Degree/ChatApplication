package dao.impl;

import dao.LoadUserMessagesDao;
import domain.client.dto.UserMessagesDto;

import javax.persistence.EntityManager;
import java.util.List;

public class LoadUserMessagesDaoImpl implements LoadUserMessagesDao {
    private static final String QUERY_PARAM_FIRST_USER_ID = "firstUserId";
    private static final String QUERY_PARAM_SECOND_USER_ID = "secondUserId";
    private static final String QUERY_STRING =
            "SELECT new domain.client.dto.UserMessagesDto(" +
                    "notif.id.notification.messageType, " +
                    "notif.id.notification.content, " +
                    "notif.id.notification.file.fileType, " +
                    "notif.id.notification.file.filePath, " +
                    "notif.id.notification.received) " +
            "FROM UserNotification notif "  +
          "WHERE ((notif.id.sender.id=:" + QUERY_PARAM_FIRST_USER_ID + ") AND (notif.receiver.id=:" + QUERY_PARAM_SECOND_USER_ID + ")) OR " +
                  "((notif.id.sender.id=:" + QUERY_PARAM_SECOND_USER_ID + ") AND (notif.receiver.id=:" +  QUERY_PARAM_FIRST_USER_ID + "))" +
          "ORDER BY notif.id.notification.sendDate DESC";

    private final EntityManager entityManager;

    public LoadUserMessagesDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<UserMessagesDto> getMessages(Long firstUserId, Long secondUserId) {
        return entityManager.createQuery(QUERY_STRING)
                .setParameter(QUERY_PARAM_FIRST_USER_ID, firstUserId)
                .setParameter(QUERY_PARAM_SECOND_USER_ID, secondUserId)
                .getResultList();
    }
}
