package domain.entities;

import javax.persistence.*;

@Entity
@Table(name="group_notifications")
public class GroupNotification {

    @EmbeddedId
    private UserNotificationId id;

    @OneToOne(targetEntity = Group.class)
    @JoinColumn(name="group_id", referencedColumnName = "id", nullable = false)
    private Group group;

    public UserNotificationId getId() {
        return id;
    }

    public void setId(UserNotificationId id) {
        this.id = id;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
