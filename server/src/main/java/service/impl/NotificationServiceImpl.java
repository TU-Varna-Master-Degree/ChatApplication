package service.impl;

import dao.GroupDao;
import dao.MessageDao;
import dao.UserDao;
import domain.client.dialogue.ServerResponse;
import domain.client.dto.GroupUserDto;
import domain.client.dto.MessageDto;
import domain.client.dto.NotificationDto;
import domain.client.dto.SendMessageDto;
import domain.entities.ChatFile;
import domain.entities.Group;
import domain.entities.Notification;
import domain.entities.UserGroup;
import domain.enums.MessageType;
import domain.enums.OperationType;
import domain.enums.StatusCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.NotificationService;
import service.SessionService;

import java.io.*;
import java.net.URLConnection;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static init.DispatcherSocket.sendResponse;

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
            return new ServerResponse(StatusCode.FAILED, "You are not part of the group!");
        }

        List<MessageDto> messages = messageDao.getGroupMessages(userId, groupId);
        List<GroupUserDto> users = groupDao.getParticipantsInGroup(groupId);

        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setUserId(userId);
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
            return new ServerResponse(StatusCode.FAILED, "The group wasn't found!");
        }

        if (!groupDao.isUserParticipateInGroup(userId, sendMessageDto.getGroupId())) {
            return new ServerResponse(StatusCode.FAILED, "You are not part of the group!");
        }

        Notification notification = new Notification();
        notification.setMessageType(sendMessageDto.getMessageType());
        notification.setSendDate(LocalDateTime.now());
        notification.setGroup(group);

        if (sendMessageDto.getMessageType().equals(MessageType.FILE)) {
            String filePath;
            try {
                filePath = saveFile(sendMessageDto.getFile(), sendMessageDto.getFileType());
                checkForImage(notification, sendMessageDto.getFile());
            } catch (IOException e) {
                return new ServerResponse(StatusCode.FAILED, "Problem appeared while creating the file.");
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

        notification.setSender(userDao.getById(userId));
        messageDao.saveNotification(notification);

        MessageDto messageDto = createMessageDto(notification);
        messageDto.setOwner(userId.equals(notification.getSender().getId()));
        saveFileToDto(notification, messageDto);

        ServerResponse<MessageDto> serverResponse = new ServerResponse<>(StatusCode.SUCCESSFUL);
        serverResponse.setOperationType(OperationType.CREATE_NOTIFICATION);
        serverResponse.setData(messageDto);

        sendNotificationsToGroup(group.getUserGroups(), serverResponse);
        return new ServerResponse(StatusCode.EMPTY);
    }

    @Override
    public ServerResponse editMessage(Long userId, SendMessageDto sendMessageDto) {
        Notification notification = messageDao.getNotificationById(sendMessageDto.getMessageId());

        if (notification == null) {
            return new ServerResponse(StatusCode.FAILED, "The message wasn't found!");
        } else if (!userId.equals(notification.getSender().getId())) {
            return new ServerResponse(StatusCode.FAILED, "You are not owner of the message!");
        }

        String oldFilePath = null;
        MessageType messageType = sendMessageDto.getMessageType();
        notification.setMessageType(messageType);

        if (messageType.equals(MessageType.FILE) || messageType.equals(MessageType.IMAGE)) {
            String filePath;
            try {
                filePath = saveFile(sendMessageDto.getFile(), sendMessageDto.getFileType());
                checkForImage(notification, sendMessageDto.getFile());
            } catch (IOException e) {
                return new ServerResponse(StatusCode.FAILED, "Problem appeared while creating the file.");
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

        messageDao.saveNotification(notification);

        if (oldFilePath != null) {
            try {
                Files.delete(Paths.get(oldFilePath));
            } catch (IOException e) {
                logger.error("Deleting old file with path \"{}\" threw exception!", oldFilePath);
            }
        }

        MessageDto messageDto = createMessageDto(notification);
        messageDto.setOwner(userId.equals(notification.getSender().getId()));
        saveFileToDto(notification, messageDto);

        ServerResponse<MessageDto> serverResponse = new ServerResponse<>(StatusCode.SUCCESSFUL);
        serverResponse.setOperationType(OperationType.EDIT_NOTIFICATION);
        serverResponse.setData(messageDto);

        sendNotificationsToGroup(notification.getGroup().getUserGroups(), serverResponse);
        return new ServerResponse(StatusCode.EMPTY);
    }

    public ServerResponse getMessage(Long userId, Long notificationId) {
        Notification notification = messageDao.getNotificationById(notificationId);
        if (notification == null) {
            return new ServerResponse(StatusCode.FAILED, "The message wasn't found!");
        }

        MessageDto messageDto = createMessageDto(notification);
        messageDto.setOwner(userId.equals(notification.getSender().getId()));
        MessageType messageType = messageDto.getMessageType();
        if (messageType.equals(MessageType.FILE) || messageType.equals(MessageType.IMAGE)) {
            saveFileToDto(notification, messageDto);
            try {
                messageDto.setFile(Files.readAllBytes(Paths.get(notification.getFile().getFilePath())));
            } catch (IOException ex) {
                logger.error("Error appeared while parsing file.");
            }
        }

        ServerResponse<MessageDto> serverResponse = new ServerResponse<>(StatusCode.SUCCESSFUL);
        serverResponse.setData(messageDto);
        return serverResponse;
    }

    private void checkForImage(Notification notification, byte[] file) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(file)) {
            String contentType = URLConnection.guessContentTypeFromStream(bis);

            if ("image".equals(contentType.split("/")[0])) {
                notification.setMessageType(MessageType.IMAGE);
            }
        } catch (IOException e) {
            logger.error("Error appeared while checking file content type");
        }
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

    private MessageDto createMessageDto(Notification notification) {
        MessageDto messageDto = new MessageDto();
        messageDto.setNotificationId(notification.getId());
        messageDto.setGroupId(notification.getGroup().getId());
        messageDto.setMessageType(notification.getMessageType());
        messageDto.setContent(notification.getContent());
        messageDto.setSendDate(notification.getSendDate());
        messageDto.setUserId(notification.getSender().getId());
        messageDto.setUsername(notification.getSender().getUsername());
        return messageDto;
    }

    private void saveFileToDto(Notification notification, MessageDto messageDto) {
        MessageType messageType = notification.getMessageType();
        if (messageType.equals(MessageType.FILE) || messageType.equals(MessageType.IMAGE)) {
            messageDto.setFileType(notification.getFile().getFileType());
            messageDto.setFileName(notification.getFile().getFileName());
        }
    }

    private void sendNotificationsToGroup(List<UserGroup> userGroups, ServerResponse<MessageDto> serverResponse) {
        userGroups.forEach(ug -> {
            SocketChannel channel = sessionService.getChannelById(ug.getId().getUser().getId());
            if (channel != null) {
                MessageDto messageDto = serverResponse.getData();
                messageDto.setOwner(messageDto.getUserId().equals(ug.getId().getUser().getId()));
                sendResponse(channel, serverResponse);
            }
        });
    }
}
