package service;

import domain.client.dialogue.ServerResponse;
import domain.client.dto.SendMessageDto;

public interface NotificationService {

    public ServerResponse getGroupNotifications(Long userId, Long groupId);

    public ServerResponse createMessage(Long userId, SendMessageDto sendMessageDto);
}
