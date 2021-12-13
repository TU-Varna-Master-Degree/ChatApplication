package domain.entities;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "groups")
public class Group extends Identity {

    @Column(nullable = false)
    private String name;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "creator_id", referencedColumnName = "id", nullable = false)
    private User creator;

    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDate;

    public Group(){};
    public Group(LocalDateTime creationDate, String name, User creator ) {
        this.name = name;
        this.creator = creator;
        this.creationDate = creationDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }
}
