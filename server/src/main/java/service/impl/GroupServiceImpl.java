package service.impl;

import dao.GroupDao;
import dao.UserDao;
import domain.client.dialogue.ServerResponse;
import domain.client.dto.AddGroupFriendsDto;
import domain.client.dto.GroupFriendDto;
import domain.client.dto.GroupDto;
import domain.client.enums.StatusCode;
import domain.entities.Group;
import domain.entities.User;
import domain.entities.UserGroup;
import domain.entities.UserGroupId;
import service.GroupService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GroupServiceImpl implements GroupService {

    private final GroupDao groupDao;
    private final UserDao userDao;

    public GroupServiceImpl(GroupDao groupDao, UserDao userDao) {
        this.groupDao = groupDao;
        this.userDao = userDao;
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
            return new ServerResponse(StatusCode.FAILED, "Не сте част от групата!");
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

        ServerResponse<Long> response = new ServerResponse<>(StatusCode.SUCCESSFUL, "Успешно добавихте новите членове на групата!");
        response.setData(group.getId());
        return response;
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
}
