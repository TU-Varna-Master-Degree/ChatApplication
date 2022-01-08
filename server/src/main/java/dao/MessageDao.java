package dao;

import domain.client.dto.MessageDto;
import domain.entities.Notification;

import java.util.List;

public interface MessageDao {

    public List<MessageDto> getGroupMessages(Long userId, Long groupId);

    public void saveNotification(Notification notification);

    public Notification getNotificationById(Long notificationId);
}
