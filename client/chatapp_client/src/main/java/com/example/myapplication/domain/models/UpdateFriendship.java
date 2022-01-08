package com.example.myapplication.domain.models;

import com.example.myapplication.domain.enums.FriendshipState;

public class UpdateFriendship {

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
