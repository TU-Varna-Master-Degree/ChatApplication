package dao.impl;

import dao.LoadGroupMessagesDao;
import domain.client.dto.GroupMessagesDto;

import javax.persistence.EntityManager;
import java.util.List;

public class LoadGroupMessagesDaoImpl implements LoadGroupMessagesDao {
    private static final String QUERY_PARAM_GROUP_ID = ":groupId";
    private static final String SUBQUERY_MESSAGES_STRING =
            "SELECT new domain.client.dto.GroupMessagesDto.Messages(" +
                    "notif.id.notification.messageType," +
                    "notif.id.notification.content," +
                    "notif.id.notification.sendDate," +
                    "notif.id.notification.file.filePath," +
                    "notif.id.notification.received," +
                    "notif.id.sender.username) " +
            "FROM GroupNotification AS notif " +
            "WHERE notif.group.id=" + QUERY_PARAM_GROUP_ID + " " +
            "ORDER BY notif.id.notification.sendDate DESC";
    private static final String QUERY_STRING =
            "SELECT new domain.dto.GroupMessagesDto(" +
                    "group.name," +
                    "group.creationDate," +
                    SUBQUERY_MESSAGES_STRING +")" +
            "FROM Group AS group" +
            "WHERE group.id=" + QUERY_PARAM_GROUP_ID;

    private final EntityManager manager;

    public LoadGroupMessagesDaoImpl(EntityManager manager) {
        this.manager = manager;
    }

    @Override
    public List<GroupMessagesDto> getGroupMessages(Long groupId) {
        return manager.createQuery( QUERY_STRING )
                .setParameter(QUERY_PARAM_GROUP_ID, groupId)
                .getResultList();
    }
}
