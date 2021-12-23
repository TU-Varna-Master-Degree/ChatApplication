package dao;

import domain.client.dto.FindFriendDto;
import domain.client.dto.FriendshipDto;
import domain.client.enums.FriendshipState;
import domain.entities.Friendship;

import java.util.List;

public interface FriendshipDao {

    public List<FriendshipDto> getFriendships(Long userId);

    public Friendship getFriendship(long senderId, long receiverId);

    public void createFriendship(long senderId, long receiverId);

    public void changeFriendshipState(Friendship friendship, FriendshipState newState);

    public List<FindFriendDto> findFriendByName(Long userId, String username);
}
