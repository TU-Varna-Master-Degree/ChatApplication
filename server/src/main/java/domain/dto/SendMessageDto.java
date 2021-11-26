package domain.dto;

public class SendMessageDto {
    private int messageType;
    private String content;
    private int fileType;
    private String filePath;
    private int fileSize;
    private Long senderId;
    private Long receiverId;

    public SendMessageDto(int messageType, String content, int fileType, String filePath, int fileSize, Long senderId, Long receiverId) {
        this.messageType = messageType;
        this.content = content;
        this.fileType = fileType;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.senderId = senderId;
        this.receiverId = receiverId;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }
}
