package domain.entities;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
public class GroupNotificationId implements Serializable {

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "sender_id", referencedColumnName = "id", nullable = false)
    private User sender;

    @OneToOne(targetEntity = Notification.class)
    @JoinColumn(name = "notification_id", referencedColumnName = "id", nullable = false)
    private Notification notification;

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }
}