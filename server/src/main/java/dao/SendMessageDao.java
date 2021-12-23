package dao;

import domain.client.dto.SendMessageDto;


public interface SendMessageDao {
    void send( SendMessageDto data );
}
