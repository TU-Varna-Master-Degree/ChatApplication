package domain.client.dto;

import java.time.LocalDateTime;

public class WhatGroupDto {
    String groupName;
    LocalDateTime foundation;

    public WhatGroupDto(String groupName, LocalDateTime foundation) {
        this.groupName = groupName;
        this.foundation = foundation;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public LocalDateTime getFoundation() {
        return foundation;
    }

    public void setFoundation(LocalDateTime foundation) {
        this.foundation = foundation;
    }
}
