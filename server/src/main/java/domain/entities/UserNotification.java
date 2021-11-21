package domain.entities;

import javax.persistence.*;

@Entity
@Table(name="user_notifications")
public class UserNotification {

    @EmbeddedId
    private UserNotificationId id;

    @OneToOne(targetEntity = User.class)
    @JoinColumn(name = "receiver_id", referencedColumnName = "id", nullable = false)
    private User receiver;

    public UserNotificationId getId() {
        return id;
    }

    public void setId(UserNotificationId id) {
        this.id = id;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }
}
