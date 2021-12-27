package service.impl;

import dao.FriendshipDao;
import dao.GroupDao;
import domain.client.dialogue.ServerResponse;
import domain.client.dto.FindFriendDto;
import domain.client.dto.FriendshipDto;
import domain.client.dto.UpdateFriendshipDto;
import domain.client.enums.FriendshipState;
import domain.client.enums.StatusCode;
import domain.entities.Friendship;
import domain.entities.Group;
import service.FriendshipService;
import service.GroupService;
import service.SessionService;

import java.util.Arrays;
import java.util.List;

public class FriendshipServiceImpl implements FriendshipService {

    private final FriendshipDao friendshipDao;
    private final GroupService groupService;

    public FriendshipServiceImpl(FriendshipDao friendshipDao, GroupService groupService) {
        this.friendshipDao = friendshipDao;
        this.groupService = groupService;
    }

    @Override
    public ServerResponse<List<FriendshipDto>> getFriendships(Long userId) {
        List<FriendshipDto> friendships = this.friendshipDao.getFriendships(userId);
        ServerResponse<List<FriendshipDto>> serverResponse = new ServerResponse<>(StatusCode.SUCCESSFUL);
        serverResponse.setData(friendships);
        return serverResponse;
    }

    @Override
    public ServerResponse createFriendship(Long senderId, Long receiverId) {
        if (friendshipDao.getFriendship(senderId, receiverId) != null) {
            return new ServerResponse(StatusCode.FAILED, "Приятелската заявка вече съществува!");
        }

        friendshipDao.createFriendship(senderId, receiverId);
        return new ServerResponse(StatusCode.SUCCESSFUL, "Приятелската заявка беше успешно създадена!");
    }

    @Override
    public ServerResponse updateFriendship(Long senderId, UpdateFriendshipDto updateFriendshipDto) {
        Long receiverId = updateFriendshipDto.getReceiverId();
        Friendship friendship = friendshipDao.getFriendship(senderId, receiverId);

        if (friendship == null) {
            return new ServerResponse(StatusCode.FAILED, "Приятелската връзка не съществува!");
        }

        friendshipDao.changeFriendshipState(friendship, updateFriendshipDto.getFriendshipState());

        ServerResponse<Long> response = new ServerResponse<>(StatusCode.SUCCESSFUL, "Успешно променихте състоянието на приятелската връзка!");
        if (updateFriendshipDto.getFriendshipState().equals(FriendshipState.ACCEPTED)) {
            Group group = groupService.createUserGroup(Arrays.asList(friendship.getId().getReceiver(), friendship.getId().getSender()));
            response.setData(group.getId());
        }

        return response;
    }

    @Override
    public ServerResponse<List<FindFriendDto>> findFriends(Long userId, String username) {
        List<FindFriendDto> friends = friendshipDao.findFriendByName(userId, username);
        ServerResponse<List<FindFriendDto>> response = new ServerResponse<>(StatusCode.SUCCESSFUL);
        response.setData(friends);
        return response;
    }
}
