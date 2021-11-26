package dao;

import java.util.List;

/**
 * 15. Зареждане на съобщенията на групата - Димитър Годеманов
 * - приема group id
 * - връща обект с group_name, group_creationdate, и списък с message_type, content, send_date, file_path?, received, sender_username,  (подредени по send_date DESC)
 * */
public interface LoadGroupMessagesDao {
    List getGroupMessages(Long groupId);
}
