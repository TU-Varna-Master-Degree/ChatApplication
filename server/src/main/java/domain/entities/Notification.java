package domain.entities;

import domain.enums.MessageType;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification extends Identity {

    @Enumerated(EnumType.STRING)
    @Column(name = "message_type", nullable = false)
    private MessageType messageType;

    @Column(name = "content", length = 1023)
    private String content;

    @Column(name = "send_date", nullable = false)
    private LocalDateTime sendDate;

    @OneToOne(targetEntity = ChatFile.class, cascade = CascadeType.PERSIST, orphanRemoval = true)
    @JoinColumn(name = "file_id", referencedColumnName = "id")
    private ChatFile file;

    @ManyToOne(targetEntity = Group.class)
    @JoinColumn(name = "group_id", referencedColumnName = "id", nullable = false)
    private Group group;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "sender_id", referencedColumnName = "id", nullable = false)
    private User sender;

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

    public ChatFile getFile() {
        return file;
    }

    public void setFile(ChatFile file) {
        this.file = file;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }
}
