package domain.entities;

import domain.client.enums.MessageType;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification extends Identity {

    @Enumerated(EnumType.STRING)
    @Column(name = "message_type", nullable = false)
    private MessageType messageType;

    private String content;

    @Column(name = "send_date", nullable = false)
    private LocalDateTime sendDate;

    @Column(nullable = false)
    private boolean received;

    @OneToOne(targetEntity = ChatFile.class)
    @JoinColumn(name = "file_id", referencedColumnName = "id", nullable = false)
    private ChatFile file;

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

    public LocalDateTime getSendDate() {
        return sendDate;
    }

    public void setSendDate(LocalDateTime sendDate) {
        this.sendDate = sendDate;
    }

    public boolean isReceived() {
        return received;
    }

    public void setReceived(boolean received) {
        this.received = received;
    }

    public ChatFile getFile() {
        return file;
    }

    public void setFile(ChatFile file) {
        this.file = file;
    }
}
