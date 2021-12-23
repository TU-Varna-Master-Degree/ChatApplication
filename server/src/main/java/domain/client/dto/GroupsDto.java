package domain.client.dto;

import java.time.LocalDateTime;

public class GroupsDto {
    private Long groupId;
    private String groupName;
    private LocalDateTime userJoinDate;

    public GroupsDto(Long groupId, String groupName, LocalDateTime userJoinDate) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.userJoinDate = userJoinDate;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public LocalDateTime getUserJoinDate() {
        return userJoinDate;
    }

    public void setUserJoinDates(LocalDateTime userJoinDate) {
        this.userJoinDate = userJoinDate;
    }
}

