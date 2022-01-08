package domain.client.dto;

import domain.enums.FriendshipState;

public class FriendshipDto {

    private Long groupId;

    private Long senderId;

    private String senderUsername;

    private FriendshipState state;

    public FriendshipDto() {
    }

    public FriendshipDto(Long groupId, Long senderId, String senderUsername, FriendshipState state) {
        this.groupId = groupId;
        this.senderId = senderId;
        this.senderUsername = senderUsername;
        this.state = state;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
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
