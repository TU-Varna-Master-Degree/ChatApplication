package dao;

import domain.client.dto.MessageDto;
import domain.entities.GroupNotification;
import domain.entities.Notification;

import java.util.List;

public interface MessageDao {

    public List<MessageDto> getGroupMessages(Long userId, Long groupId);

    public void saveNotification(Notification notification);

    public void saveGroupNotification(GroupNotification groupNotification);

    public GroupNotification getGroupNotificationById(Long senderId, Long notificationId);
}
