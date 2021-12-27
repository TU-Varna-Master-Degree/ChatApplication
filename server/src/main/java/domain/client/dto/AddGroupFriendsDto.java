package domain.client.dto;

import java.io.Serializable;
import java.util.List;

public class AddGroupFriendsDto implements Serializable {

    private Long groupId;

    private List<Long> userIds;

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public List<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds;
    }
}
