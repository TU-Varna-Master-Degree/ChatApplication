package domain.client.dto;

import domain.client.enums.MessageType;

public class BroadcastToGroupDto {
    private MessageType messageType;
    private String content;
    private String fileType;
    private String filePath;
    private String fileSize;
    private Long groupId;
    private Long senderId;

    public BroadcastToGroupDto(MessageType messageType, String content, String fileType, String filePath, String fileSize, Long groupId, Long senderId) {
        this.messageType = messageType;
        this.content = content;
        this.fileType = fileType;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.groupId = groupId;
        this.senderId = senderId;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }
}
