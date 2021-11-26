package dao;

import domain.dto.SendMessageDto;

/**
 * 13. Изпращане на съобщение - Димитър Годеманов
 * - приема обект, който съдържа (message_type, content, file_type, file_path, file_size, sender_id, receiver_id)
 * - запазва обекта, като попълва send_date и received в базата и връща void
 * */
public interface SendMessageDao {
    void send( SendMessageDto data );
}
