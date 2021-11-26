package domain.dto;

import java.time.LocalDate;

public class GroupsDto {
    private Long groupId;
    private String groupName;
    private LocalDate userJoinDate;

    public GroupsDto(Long groupId, String groupName, LocalDate userJoinDate) {
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

    public LocalDate getUserJoinDate() {
        return userJoinDate;
    }

    public void setUserJoinDates(LocalDate userJoinDate) {
        this.userJoinDate = userJoinDate;
    }
}

