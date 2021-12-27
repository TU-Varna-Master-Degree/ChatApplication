package domain.entities;

import javax.persistence.*;

@Entity
@Table(name="group_notifications")
public class GroupNotification {

    @EmbeddedId
    private GroupNotificationId id;

    @ManyToOne(targetEntity = Group.class)
    @JoinColumn(name="group_id", referencedColumnName = "id", nullable = false)
    private Group group;

    public GroupNotificationId getId() {
        return id;
    }

    public void setId(GroupNotificationId id) {
        this.id = id;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
