package domain.entities;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name="user_group_users")
public class UserGroupUser {

    // Fields
    @EmbeddedId
    private UserGroupUserId id;

    @Column(nullable = false)
    private LocalDate joinDate;

    // Get-Set
    public UserGroupUserId getId() {
        return id;
    }

    public void setId(UserGroupUserId id) {
        this.id = id;
    }
}
