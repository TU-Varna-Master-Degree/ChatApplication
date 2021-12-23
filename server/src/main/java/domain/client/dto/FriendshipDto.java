package domain.client.dto;

import domain.client.enums.FriendshipState;

import java.io.Serializable;

public class FriendshipDto implements Serializable {

    private String receiverUsername;

    private String senderUsername;

    private FriendshipState state;

    public FriendshipDto(String receiverUsername, String senderUsername, FriendshipState state) {
        this.receiverUsername = receiverUsername;
        this.senderUsername = senderUsername;
        this.state = state;
    }

    public String getReceiverUsername() {
        return receiverUsername;
    }

    public void setReceiverUsername(String receiverUsername) {
        this.receiverUsername = receiverUsername;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public FriendshipState getState() {
        return state;
    }

    public void setState(FriendshipState state) {
        this.state = state;
    }
}
