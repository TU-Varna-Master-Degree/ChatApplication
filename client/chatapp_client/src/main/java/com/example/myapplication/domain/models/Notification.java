package com.example.myapplication.domain.models;

import java.util.List;

public class Notification {

    private List<Message> messages;

    private List<GroupUser> users;

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public List<GroupUser> getUsers() {
        return users;
    }

    public void setUsers(List<GroupUser> users) {
        this.users = users;
    }
}
