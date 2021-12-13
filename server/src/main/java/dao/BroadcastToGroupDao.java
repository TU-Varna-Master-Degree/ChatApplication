package dao;

import domain.dto.BroadcastToGroupDto;


public interface BroadcastToGroupDao {
    void broadcast(BroadcastToGroupDto message);
}
