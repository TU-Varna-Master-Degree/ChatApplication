package domain.entities;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "groups")
public class Group extends Identity {

    @ManyToOne(targetEntity = Group.class)
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    private Group parent;

    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDate;

    @OneToMany(targetEntity = Notification.class, mappedBy = "group")
    private List<Notification> notifications;

    @OneToMany(targetEntity = UserGroup.class, mappedBy = "id.group", cascade = CascadeType.PERSIST)
    private List<UserGroup> userGroups;

    public Group getParent() {
        return parent;
    }

    public void setParent(Group parent) {
        this.parent = parent;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

    public List<UserGroup> getUserGroups() {
        return userGroups;
    }

    public void setUserGroups(List<UserGroup> userGroups) {
        this.userGroups = userGroups;
    }
}
