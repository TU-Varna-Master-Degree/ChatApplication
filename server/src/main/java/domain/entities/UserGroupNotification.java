package domain.entities;

import javax.persistence.*;


@Entity
@Table(name="user_group_notifications")
public class UserGroupNotification {
    /// Fields
    @EmbeddedId
    private NotificationId id;

    @OneToOne(targetEntity = UserGroup.class)
    @JoinColumn(name="user_groups", referencedColumnName = "id", nullable = false)
    private UserGroup group;

    /// Get-Set
    public NotificationId getId() {
        return id;
    }

    public void setId(NotificationId id) {
        this.id = id;
    }

    public UserGroup getGroup() {
        return group;
    }

    public void setGroup(UserGroup group) {
        this.group = group;
    }
}
