package service;

import domain.client.dialogue.ServerResponse;
import domain.client.dto.FindFriendDto;
import domain.client.dto.FriendshipDto;
import domain.client.dto.UpdateFriendshipDto;

import java.util.List;

public interface FriendshipService {

    public ServerResponse<List<FriendshipDto>> getFriendships(Long userId);

    public ServerResponse createFriendship(Long senderId, Long receiverId);

    public ServerResponse updateFriendship(Long senderId, UpdateFriendshipDto updateFriendshipDto);

    public ServerResponse<List<FindFriendDto>> findFriends(Long userId, String username);
}
