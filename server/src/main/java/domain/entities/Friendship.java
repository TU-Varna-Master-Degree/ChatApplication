package domain.entities;

import domain.enums.FriendshipState;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="friendship_relations")
public class Friendship {

    @EmbeddedId
    private FriendshipId id;

    @Enumerated(EnumType.STRING)
    @Column(name = "friendship_state", nullable = false)
    private FriendshipState friendshipState;

    @Column(name = "updated_state_date", nullable = false)
    private LocalDateTime updatedStateDate;

    public FriendshipId getId() {
        return id;
    }

    public void setId(FriendshipId id) {
        this.id = id;
    }

    public FriendshipState getFriendshipState() {
        return friendshipState;
    }

    public void setFriendshipState(FriendshipState friendshipState) {
        this.friendshipState = friendshipState;
    }

    public LocalDateTime getUpdatedStateDate() {
        return updatedStateDate;
    }

    public void setUpdatedStateDate(LocalDateTime updatedStateDate) {
        this.updatedStateDate = updatedStateDate;
    }
}
