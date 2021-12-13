package domain.dto;

import domain.enums.MessageType;

public class UserMessagesDto {
    private MessageType messageType;
    private String content;
    private String filePath;
    private boolean hasBeenReceived;

    public UserMessagesDto(MessageType messageType, String content, String fileType, String filePath, boolean hasBeenReceived) {
        this.messageType = messageType;
        this.content = content;
        this.filePath = filePath;
        this.hasBeenReceived = hasBeenReceived;
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

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public boolean isHasBeenReceived() {
        return hasBeenReceived;
    }

    public void setHasBeenReceived(boolean hasBeenReceived) {
        this.hasBeenReceived = hasBeenReceived;
    }
}
