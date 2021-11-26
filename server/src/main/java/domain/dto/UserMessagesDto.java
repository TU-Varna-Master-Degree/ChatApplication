package domain.dto;

import domain.enums.MessageType;

public class UserMessagesDto {
    private MessageType messageType;
    private byte[] content;
    private String filePath;
    private boolean hasBeenReceived;

    public UserMessagesDto(MessageType messageType, byte[] content, int fileType, String filePath, boolean hasBeenReceived) {
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

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
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
