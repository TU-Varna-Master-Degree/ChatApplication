package service;

import domain.client.dialogue.ServerResponse;
import domain.client.dto.AddGroupFriendsDto;
import domain.client.dto.GroupFriendDto;
import domain.client.dto.GroupDto;
import domain.entities.Group;
import domain.entities.User;

import java.util.List;

public interface GroupService {

    public ServerResponse<List<GroupDto>> getUserGroups(Long userId);

    public ServerResponse<List<GroupFriendDto>> getGroupFriends(Long userId, Long groupId);

    public Group createUserGroup(List<User> users);

    public ServerResponse addUsersToGroup(Long userId, AddGroupFriendsDto addGroupFriendsDto);
}
