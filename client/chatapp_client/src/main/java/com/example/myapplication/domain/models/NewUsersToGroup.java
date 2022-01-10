package com.example.myapplication.domain.models;

import java.util.List;

public class NewUsersToGroup {

    private Long oldGroupId;

    private Long newGroupId;

    private List<GroupUser> newUsers;

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

    public List<GroupUser> getNewUsers() {
        return newUsers;
    }

    public void setNewUsers(List<GroupUser> newUsers) {
        this.newUsers = newUsers;
    }
}
