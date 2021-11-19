package domain.entities;


import javax.persistence.*;

@Entity
@Table(name="user_notifications")
public class UserNotification {
    /// Fields
    @EmbeddedId
    private NotificationId id;

    @OneToOne(targetEntity = User.class)
    @JoinColumn(name="users", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private User user;

    // Get-Set
    public User getGroup() {
        return user;
    }

    public void setGroup(User user) {
        this.user = user;
    }

    public NotificationId getId() {
        return id;
    }

    public void setId(NotificationId id) {
        this.id = id;
    }
}
