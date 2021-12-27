package dao;

import domain.client.dto.GroupFriendDto;
import domain.client.dto.GroupUserDto;
import domain.entities.Group;

import java.util.List;

public interface GroupDao {

    public List<Object[]> getUserGroups(Long userId);

    public List<GroupFriendDto> getGroupFriends(Long userId, Long groupId);

    public void save(Group group);

    public Group getById(Long groupId);

    public boolean isUserParticipateInGroup(Long userId, Long groupId);

    public List<GroupUserDto> getParticipantsInGroup(Long groupId);
}
