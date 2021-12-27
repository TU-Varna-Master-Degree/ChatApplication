package domain.client.dto;

import domain.client.enums.MessageType;

import java.io.Serializable;
import java.time.LocalDateTime;

public class MessageDto implements Serializable {

    private Long notificationId;
    private String content;
    private MessageType messageType;
    private LocalDateTime sendDate;
    private transient String filePath;
    private String fileName;
    private String fileType;
    private byte[] file;

    public MessageDto(Long notificationId, String content, MessageType messageType,
                      LocalDateTime sendDate, String filePath, String fileName, String fileType) {
        this.notificationId = notificationId;
        this.content = content;
        this.messageType = messageType;
        this.sendDate = sendDate;
        this.filePath = filePath;
        this.fileName = fileName;
        this.fileType = fileType;
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

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
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
}
