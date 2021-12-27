package domain.client.dto;

import java.io.Serializable;
import java.util.List;

public class NotificationDto implements Serializable {

    private List<MessageDto> messages;

    private List<GroupUserDto> users;

    public List<MessageDto> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageDto> messages) {
        this.messages = messages;
    }

    public List<GroupUserDto> getUsers() {
        return users;
    }

    public void setUsers(List<GroupUserDto> users) {
        this.users = users;
    }
}
