package service.impl;

import dao.GroupDao;
import dao.MessageDao;
import dao.UserDao;
import domain.client.dialogue.ServerResponse;
import domain.client.dto.*;
import domain.client.enums.MessageType;
import domain.client.enums.OperationType;
import domain.client.enums.StatusCode;
import domain.entities.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    private final Logger logger;

    public NotificationServiceImpl(MessageDao messageDao, GroupDao groupDao,
                                   UserDao userDao, SessionService sessionService) {
        this.messageDao = messageDao;
        this.groupDao = groupDao;
        this.userDao = userDao;
        this.sessionService = sessionService;
        this.logger = LogManager.getLogger();
    }

    @Override
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

    @Override
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
            String filePath;
            try {
                filePath = saveFile(sendMessageDto.getFile(), sendMessageDto.getFileType());
            } catch (IOException e) {
                return new ServerResponse(StatusCode.FAILED, "Проблем със създаването на файла.");
            }

            ChatFile chatFile = new ChatFile();
            chatFile.setFileType(sendMessageDto.getFileType());
            chatFile.setFileName(sendMessageDto.getFileName());
            chatFile.setFilePath(filePath);
            chatFile.setFileSize(sendMessageDto.getFile().length);

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
        groupMessageDto.setMessageId(notification.getId());
        groupMessageDto.setGroupId(group.getId());
        groupMessageDto.setMessageType(notification.getMessageType());
        groupMessageDto.setContent(notification.getContent());
        groupMessageDto.setSendDate(notification.getSendDate());
        groupMessageDto.setUserId(groupNotificationId.getSender().getId());
        groupMessageDto.setUsername(groupNotificationId.getSender().getUsername());
        saveFileToDto(sendMessageDto, notification, groupMessageDto);

        ServerResponse<GroupMessageDto> serverResponse = new ServerResponse<>(StatusCode.SUCCESSFUL);
        serverResponse.setOperationType(OperationType.CREATE_NOTIFICATION);
        serverResponse.setData(groupMessageDto);

        sendNotificationsToGroup(group.getUserGroups(), serverResponse);
        return new ServerResponse(StatusCode.EMPTY);
    }

    @Override
    public ServerResponse editMessage(Long userId, SendMessageDto sendMessageDto) {
        GroupNotification groupNotification = messageDao.getGroupNotificationById(userId, sendMessageDto.getMessageId());

        if (groupNotification == null) {
            return new ServerResponse(StatusCode.FAILED, "Съобщението не е открито!");
        }

        Notification notification = groupNotification.getId().getNotification();
        String oldFilePath = null;

        if (sendMessageDto.getMessageType().equals(MessageType.FILE)) {
            String filePath;
            try {
                filePath = saveFile(sendMessageDto.getFile(), sendMessageDto.getFileType());
            } catch (IOException e) {
                return new ServerResponse(StatusCode.FAILED, "Проблем със създаването на файла.");
            }

            ChatFile chatFile = notification.getFile();
            if (chatFile == null) {
                chatFile = new ChatFile();
            } else {
                oldFilePath = chatFile.getFilePath();
            }

            chatFile.setFileType(sendMessageDto.getFileType());
            chatFile.setFileName(sendMessageDto.getFileName());
            chatFile.setFilePath(filePath);
            chatFile.setFileSize(sendMessageDto.getFile().length);

            notification.setFile(chatFile);
            notification.setContent(null);
        } else {
            notification.setContent(sendMessageDto.getContent());
            if (notification.getFile() != null) {
                oldFilePath = notification.getFile().getFilePath();
                notification.setFile(null);
            }
        }

        notification.setMessageType(sendMessageDto.getMessageType());
        messageDao.saveNotification(notification);

        if (oldFilePath != null) {
            try {
                Files.delete(Paths.get(oldFilePath));
            } catch (IOException e) {
                logger.error("Deleting old file with path \"{}\" threw exception!", oldFilePath);
            }
        }

        GroupMessageDto groupMessageDto = new GroupMessageDto();
        groupMessageDto.setMessageId(notification.getId());
        groupMessageDto.setGroupId(groupNotification.getGroup().getId());
        groupMessageDto.setMessageType(notification.getMessageType());
        groupMessageDto.setContent(notification.getContent());
        groupMessageDto.setSendDate(notification.getSendDate());
        groupMessageDto.setUserId(groupNotification.getId().getSender().getId());
        groupMessageDto.setUsername(groupNotification.getId().getSender().getUsername());
        saveFileToDto(sendMessageDto, notification, groupMessageDto);

        ServerResponse<GroupMessageDto> serverResponse = new ServerResponse<>(StatusCode.SUCCESSFUL);
        serverResponse.setOperationType(OperationType.EDIT_NOTIFICATION);
        serverResponse.setData(groupMessageDto);

        sendNotificationsToGroup(groupNotification.getGroup().getUserGroups(), serverResponse);
        return new ServerResponse(StatusCode.EMPTY);
    }

    private String saveFile(byte[] file, String fileType) throws IOException {
        String fileName = UUID.randomUUID().toString();
        Path relativePath = Paths.get("src", "main", "resources", "files",
                String.format("%s.%s", fileName, fileType));
        Path absolutePath = relativePath.toAbsolutePath();

        try (BufferedInputStream bis = new BufferedInputStream(new ByteArrayInputStream(file));
             BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(absolutePath.toFile()))) {
            byte[] bytes = new byte[1024];
            int readBytes;

            while ((readBytes = bis.read(bytes)) > -1) {
                bos.write(bytes, 0, readBytes);
            }
        }

        return absolutePath.toString();
    }

    private void saveFileToDto(SendMessageDto sendMessageDto, Notification notification, GroupMessageDto groupMessageDto) {
        if (notification.getMessageType().equals(MessageType.FILE)) {
            groupMessageDto.setFileType(notification.getFile().getFileType());
            groupMessageDto.setFileName(notification.getFile().getFileName());
            groupMessageDto.setFile(sendMessageDto.getFile());
        }
    }

    private void sendNotificationsToGroup(List<UserGroup> userGroups, ServerResponse<GroupMessageDto> serverResponse) {
        userGroups.forEach(ug -> {
            SocketChannel channel = sessionService.getChannelById(ug.getId().getUser().getId());
            if (channel != null) {
                sendResponse(channel, serverResponse);
            }
        });
    }
}
