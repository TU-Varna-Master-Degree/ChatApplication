package dao;

import domain.dto.FindFriendDto;

import java.util.List;

public interface FindFriendDao {

    public List<FindFriendDto> findFriendByName(Long userId, String username);

}
