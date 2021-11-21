package domain.entities;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
public class UserGroupId implements Serializable {

    @ManyToOne(targetEntity = Group.class)
    @JoinColumn(name = "group_id", referencedColumnName = "id", nullable = false)
    private Group group;

    @OneToOne(targetEntity = User.class)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
