package dao;

import domain.dto.BroadcastToGroupDto;

/**
 * 16. Изпращане на съобщение на група - Димитър Годеманов
 * - приема обект, който съдържа (message_type, content, file_type, file_path, file_size, group_id, sender_id)
 * - запазва обекта, като попълва send_date и received? в базата и връща void
 * */

public interface BroadcastToGroupDao {
    void broadcast(BroadcastToGroupDto message);
}
