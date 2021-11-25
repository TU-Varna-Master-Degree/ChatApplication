package domain.dto;

import domain.enums.FriendshipState;

public class FriendshipDto {

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
