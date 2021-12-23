package domain.client.dto;

import domain.client.enums.FriendshipState;

import java.io.Serializable;

public class UpdateFriendshipDto implements Serializable {

    private Long receiverId;

    private FriendshipState friendshipState;

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public FriendshipState getFriendshipState() {
        return friendshipState;
    }

    public void setFriendshipState(FriendshipState friendshipState) {
        this.friendshipState = friendshipState;
    }
}
