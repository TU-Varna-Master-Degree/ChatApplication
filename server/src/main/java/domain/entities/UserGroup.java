package domain.entities;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name="user_groups")
public class UserGroup {

    @EmbeddedId
    private UserGroupId id;

    @Column(name = "join_date", nullable = false)
    private LocalDateTime joinDate;

    public UserGroupId getId() {
        return id;
    }

    public void setId(UserGroupId id) {
        this.id = id;
    }

    public LocalDateTime getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(LocalDateTime joinDate) {
        this.joinDate = joinDate;
    }
}
