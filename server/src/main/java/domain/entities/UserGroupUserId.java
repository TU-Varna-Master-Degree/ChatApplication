package domain.entities;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
public class UserGroupUserId implements Serializable {
    /// Fields
    @ManyToOne(targetEntity = UserGroup.class)
    @JoinColumn(name = "user_groups", referencedColumnName = "id", nullable = false)
    private UserGroup group;

    @OneToOne(targetEntity = User.class)
    @JoinColumn(name = "users", referencedColumnName = "id", nullable = false)
    private User user;

    /// Get-Set
    public UserGroup getGroup() {
        return group;
    }

    public void setGroup(UserGroup group) {
        this.group = group;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
