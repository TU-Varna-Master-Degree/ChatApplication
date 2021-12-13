package dao;

import domain.dto.SendMessageDto;


public interface SendMessageDao {
    void send( SendMessageDto data );
}
