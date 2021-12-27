package service.impl;

import dao.GroupDao;
import dao.MessageDao;
import dao.UserDao;
import domain.client.dialogue.ServerResponse;
import domain.client.dto.*;
import domain.client.enums.MessageAction;
import domain.client.enums.MessageType;
import domain.client.enums.OperationType;
import domain.client.enums.StatusCode;
import domain.entities.*;
import service.NotificationService;
import service.SessionService;

import java.io.*;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static init.DispatcherServlet.sendResponse;

public class NotificationServiceImpl implements NotificationService {

    private final MessageDao messageDao;
    private final GroupDao groupDao;
    private final UserDao userDao;
    private final SessionService sessionService;

    public NotificationServiceImpl(MessageDao messageDao, GroupDao groupDao,
                                   UserDao userDao, SessionService sessionService) {
        this.messageDao = messageDao;
        this.groupDao = groupDao;
        this.userDao = userDao;
        this.sessionService = sessionService;
    }

    public ServerResponse getGroupNotifications(Long userId, Long groupId) {
        if (!groupDao.isUserParticipateInGroup(userId, groupId)) {
            return new ServerResponse(StatusCode.FAILED, "Не сте включен в групата!");
        }

        List<MessageDto> messages = messageDao.getGroupMessages(userId, groupId);
        messages.stream()
            .filter(m -> m.getMessageType().equals(MessageType.FILE))
            .forEach(m -> {
                try {
                    m.setFile(Files.readAllBytes(Paths.get(m.getFilePath())));
                } catch (IOException ex) {
                    m.setFile("Грешка с прочитането на файла!".getBytes());
                }
            });

        List<GroupUserDto> users = groupDao.getParticipantsInGroup(groupId);

        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setMessages(messages);
        notificationDto.setUsers(users);

        ServerResponse<NotificationDto> response = new ServerResponse<>(StatusCode.SUCCESSFUL);
        response.setData(notificationDto);
        return response;
    }

    public ServerResponse createMessage(Long userId, SendMessageDto sendMessageDto) {
        Group group = groupDao.getById(sendMessageDto.getGroupId());
        if (group == null) {
            return new ServerResponse(StatusCode.FAILED, "Не беше намерена такава група!");
        }

        if (!groupDao.isUserParticipateInGroup(userId, sendMessageDto.getGroupId())) {
            return new ServerResponse(StatusCode.FAILED, "Не сте част от тази група!");
        }

        Notification notification = new Notification();
        notification.setMessageType(sendMessageDto.getMessageType());
        notification.setSendDate(LocalDateTime.now());

        if (sendMessageDto.getMessageType().equals(MessageType.FILE)) {
            String fileName = UUID.randomUUID().toString();
            Path relativePath = Paths.get("src", "main", "resources", "files",
                    String.format("%s.%s", fileName, sendMessageDto.getFileType()));
            Path absolutePath = relativePath.toAbsolutePath();

            ChatFile chatFile = new ChatFile();
            chatFile.setFileType(sendMessageDto.getFileType());
            chatFile.setFileName(sendMessageDto.getFileName());
            chatFile.setFilePath(absolutePath.toString());
            chatFile.setFileSize(sendMessageDto.getFile().length);

            try (BufferedInputStream bis = new BufferedInputStream(new ByteArrayInputStream(sendMessageDto.getFile()));
                 BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(absolutePath.toFile()))) {
                byte[] bytes = new byte[1024];
                int readBytes;

                while ((readBytes = bis.read(bytes)) > -1) {
                    bos.write(bytes, 0, readBytes);
                }
            } catch (IOException e) {
                return new ServerResponse(StatusCode.FAILED, "Проблем със създаването на файла.");
            }

            notification.setFile(chatFile);
        } else {
            notification.setContent(sendMessageDto.getContent());
        }

        GroupNotificationId groupNotificationId = new GroupNotificationId();
        groupNotificationId.setNotification(notification);
        groupNotificationId.setSender(userDao.getById(userId));

        GroupNotification groupNotification = new GroupNotification();
        groupNotification.setId(groupNotificationId);
        groupNotification.setGroup(group);
        messageDao.saveGroupNotification(groupNotification);

        GroupMessageDto groupMessageDto = new GroupMessageDto();
        groupMessageDto.setMessageAction(MessageAction.CREATE);
        groupMessageDto.setMessageId(notification.getId());
        groupMessageDto.setGroupId(group.getId());
        groupMessageDto.setMessageType(notification.getMessageType());
        groupMessageDto.setContent(notification.getContent());
        groupMessageDto.setSendDate(notification.getSendDate());
        groupMessageDto.setUserId(groupNotificationId.getSender().getId());
        groupMessageDto.setUsername(groupNotificationId.getSender().getUsername());

        if (notification.getMessageType().equals(MessageType.FILE)) {
            groupMessageDto.setFileType(notification.getFile().getFileType());
            groupMessageDto.setFileName(notification.getFile().getFileName());
            groupMessageDto.setFile(sendMessageDto.getFile());
        }

        ServerResponse<GroupMessageDto> serverResponse = new ServerResponse<>(StatusCode.SUCCESSFUL);
        serverResponse.setOperationType(OperationType.CREATE_NOTIFICATION);
        serverResponse.setData(groupMessageDto);

        group.getUserGroups().forEach(ug -> {
            SocketChannel channel = sessionService.getChannelById(ug.getId().getUser().getId());
            if (channel != null) {
                sendResponse(channel, serverResponse);
            }
        });

        return new ServerResponse(StatusCode.EMPTY);
    }
}
