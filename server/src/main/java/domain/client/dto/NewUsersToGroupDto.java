package domain.client.dto;

import java.util.List;

public class NewUsersToGroupDto {

    private Long oldGroupId;

    private Long newGroupId;

    private List<GroupUserDto> newUsers;

    public Long getOldGroupId() {
        return oldGroupId;
    }

    public void setOldGroupId(Long oldGroupId) {
        this.oldGroupId = oldGroupId;
    }

    public Long getNewGroupId() {
        return newGroupId;
    }

    public void setNewGroupId(Long newGroupId) {
        this.newGroupId = newGroupId;
    }

    public List<GroupUserDto> getNewUsers() {
        return newUsers;
    }

    public void setNewUsers(List<GroupUserDto> newUsers) {
        this.newUsers = newUsers;
    }
}
