package domain.client.dto;

import domain.enums.MessageType;

import java.time.LocalDateTime;

public class MessageDto {

    private Long notificationId;
    private String content;
    private MessageType messageType;
    private LocalDateTime sendDate;
    private String fileName;
    private String fileType;
    private byte[] file;
    private Long userId;
    private String username;
    private boolean isOwner;
    private Long groupId;

    public MessageDto() {
    }

    public MessageDto(Long notificationId, String content, MessageType messageType, LocalDateTime sendDate,
                      String fileName, String fileType, Long userId, String username, Boolean isOwner, Long groupId) {
        this.notificationId = notificationId;
        this.content = content;
        this.messageType = messageType;
        this.sendDate = sendDate;
        this.fileName = fileName;
        this.fileType = fileType;
        this.userId = userId;
        this.username = username;
        this.isOwner = isOwner;
        this.groupId = groupId;
    }

    public Long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Long notificationId) {
        this.notificationId = notificationId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public LocalDateTime getSendDate() {
        return sendDate;
    }

    public void setSendDate(LocalDateTime sendDate) {
        this.sendDate = sendDate;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isOwner() {
        return isOwner;
    }

    public void setOwner(boolean owner) {
        isOwner = owner;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }
}
