package service.impl;

import dao.GroupDao;
import dao.UserDao;
import domain.client.dialogue.ServerResponse;
import domain.client.dto.*;
import domain.enums.OperationType;
import domain.enums.StatusCode;
import domain.entities.Group;
import domain.entities.User;
import domain.entities.UserGroup;
import domain.entities.UserGroupId;
import service.GroupService;
import service.SessionService;

import java.nio.channels.SocketChannel;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static init.DispatcherSocket.sendResponse;

public class GroupServiceImpl implements GroupService {

    private final GroupDao groupDao;
    private final UserDao userDao;
    private final SessionService sessionService;

    public GroupServiceImpl(GroupDao groupDao, UserDao userDao, SessionService sessionService) {
        this.groupDao = groupDao;
        this.userDao = userDao;
        this.sessionService = sessionService;
    }

    @Override
    public ServerResponse<List<GroupDto>> getUserGroups(Long userId) {
        List<Object[]> groups = groupDao.getUserGroups(userId);
        List<GroupDto> groupDtos = new ArrayList<>();

        groups.forEach(g -> {
            Group group = (Group) g[0];
            LocalDateTime lastSendMessageDate = (LocalDateTime) g[1];
            List<String> userNames = group.getUserGroups()
                .stream()
                .filter(ug -> !ug.getId().getUser().getId().equals(userId))
                .map(ug -> ug.getId()
                        .getUser()
                        .getUsername())
                .collect(Collectors.toList());

            GroupDto groupDto = new GroupDto();
            groupDto.setGroupId(group.getId());
            groupDto.setGroupUsers(userNames);
            groupDto.setLastSendMessageDate(lastSendMessageDate);
            groupDtos.add(groupDto);
        });

        ServerResponse<List<GroupDto>> response = new ServerResponse<>(StatusCode.SUCCESSFUL);
        response.setData(groupDtos);
        return response;
    }

    @Override
    public ServerResponse<List<GroupFriendDto>> getGroupFriends(Long userId, Long groupId) {
        List<GroupFriendDto> groupFriendDtos = groupDao.getGroupFriends(userId, groupId);
        ServerResponse<List<GroupFriendDto>> response = new ServerResponse<>(StatusCode.SUCCESSFUL);
        response.setData(groupFriendDtos);
        return response;
    }

    @Override
    public Group createUserGroup(List<User> users) {
        LocalDateTime creationDateTime = LocalDateTime.now();

        Group group = new Group();
        group.setCreationDate(creationDateTime);
        group.setUserGroups(new ArrayList<>());

        addUsersToGroup(group, users, creationDateTime);
        groupDao.save(group);
        return group;
    }

    @Override
    public ServerResponse addUsersToGroup(Long userId, AddGroupFriendsDto addGroupFriendsDto) {
        Group group = groupDao.getById(addGroupFriendsDto.getGroupId());
        LocalDateTime creationDateTime = LocalDateTime.now();

        if (group.getUserGroups().stream().noneMatch(u -> u.getId().getUser().getId().equals(userId))) {
            return new ServerResponse(StatusCode.FAILED, "You are not part of the group!");
        }

        if (group.getUserGroups().size() == 2) {
            Group newGroup = new Group();
            newGroup.setCreationDate(creationDateTime);
            newGroup.setParent(group);
            newGroup.setUserGroups(new ArrayList<>());

            addUsersToGroup(newGroup,
                    group.getUserGroups()
                        .stream()
                        .map(ug -> ug.getId().getUser())
                        .collect(Collectors.toList()),
                    group.getUserGroups().get(0).getJoinDate());
            group = newGroup;
        }

        List<User> users = addGroupFriendsDto.getUserIds()
            .stream()
            .map(userDao::getById)
            .collect(Collectors.toList());

        addUsersToGroup(group, users, creationDateTime);
        groupDao.save(group);

        List<GroupUserDto> groupUsers = createGroupUsers(users, creationDateTime);
        NewUsersToGroupDto newUsersToGroupDto = new NewUsersToGroupDto();
        newUsersToGroupDto.setOldGroupId(addGroupFriendsDto.getGroupId());
        newUsersToGroupDto.setNewGroupId(group.getId());
        newUsersToGroupDto.setNewUsers(groupUsers);

        ServerResponse<NewUsersToGroupDto> response = new ServerResponse<>(StatusCode.SUCCESSFUL);
        response.setOperationType(OperationType.ADD_GROUP_FRIENDS);
        response.setData(newUsersToGroupDto);
        sendNewUsersToGroup(group.getUserGroups(), response);
        return new ServerResponse(StatusCode.EMPTY);
    }

    private List<GroupUserDto> createGroupUsers(List<User> users, LocalDateTime creationDateTime) {
        return users.stream().map(u -> {
            GroupUserDto groupUserDto = new GroupUserDto();
            groupUserDto.setId(u.getId());
            groupUserDto.setUsername(u.getUsername());
            groupUserDto.setJoinDate(creationDateTime);
            return groupUserDto;
        }).collect(Collectors.toList());
    }

    private void addUsersToGroup(Group group, List<User> users, LocalDateTime creationDateTime) {
        for (User user : users) {
            UserGroupId userGroupId = new UserGroupId();
            userGroupId.setGroup(group);
            userGroupId.setUser(user);

            UserGroup userGroup = new UserGroup();
            userGroup.setJoinDate(creationDateTime);
            userGroup.setId(userGroupId);

            group.getUserGroups().add(userGroup);
        }
    }

    private void sendNewUsersToGroup(List<UserGroup> userGroups, ServerResponse<NewUsersToGroupDto> serverResponse) {
        userGroups.forEach(ug -> {
            SocketChannel channel = sessionService.getChannelById(ug.getId().getUser().getId());
            if (channel != null) {
                sendResponse(channel, serverResponse);
            }
        });
    }
}
