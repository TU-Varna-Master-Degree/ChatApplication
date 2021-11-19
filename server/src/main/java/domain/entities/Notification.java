package domain.entities;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "notifications")
public class Notification
{
    /// Fields
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private long messageType;

    @Column(nullable = false)
    private byte[] content;

    @Column(nullable = false)
    private LocalDate sendDate;

    @Column(nullable = false)
    private boolean received;

    /// Get-Set
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public LocalDate getSendDate() {
        return sendDate;
    }

    public void setSendDate(LocalDate sendDate) {
        this.sendDate = sendDate;
    }

    public boolean isReceived() {
        return received;
    }

    public void setReceived(boolean received) {
        this.received = received;
    }

    public long getMessageType() {
    return messageType;
}

    public void setMessageType(long messageType) {
        this.messageType = messageType;
    }
}
