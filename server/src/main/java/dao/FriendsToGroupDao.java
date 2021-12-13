package dao;
import domain.dto.FriendsToGroupDto;

import java.util.List;

public interface FriendsToGroupDao {
    public List<FriendsToGroupDto> FriendsForGroup(long id);
}
