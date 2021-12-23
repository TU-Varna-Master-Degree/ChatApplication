package dao;

import domain.client.dto.BroadcastToGroupDto;


public interface BroadcastToGroupDao {
    void broadcast(BroadcastToGroupDto message);
}
