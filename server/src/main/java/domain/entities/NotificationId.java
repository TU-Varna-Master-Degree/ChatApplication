package domain.entities;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.io.Serializable;

@Embeddable
public class NotificationId implements Serializable {
    /// Fields
    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "users", referencedColumnName = "id", nullable = false)
    private User sender;

    @OneToOne(targetEntity = Notification.class)
    @JoinColumn(name = "notifications", referencedColumnName = "id", nullable = false)
    private Notification notification;

    /// Get-Set
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