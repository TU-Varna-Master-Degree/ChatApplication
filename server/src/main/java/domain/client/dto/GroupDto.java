package domain.client.dto;

import java.time.LocalDateTime;
import java.util.List;

public class GroupDto {
    private Long groupId;
    private List<String> groupUsers;
    private LocalDateTime lastSendMessageDate;

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public List<String> getGroupUsers() {
        return groupUsers;
    }

    public void setGroupUsers(List<String> groupUsers) {
        this.groupUsers = groupUsers;
    }

    public LocalDateTime getLastSendMessageDate() {
        return lastSendMessageDate;
    }

    public void setLastSendMessageDate(LocalDateTime lastSendMessageDate) {
        this.lastSendMessageDate = lastSendMessageDate;
    }
}

