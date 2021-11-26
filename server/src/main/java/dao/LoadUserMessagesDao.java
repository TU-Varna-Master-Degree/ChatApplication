package dao;

import java.util.List;

/**
 * 12. Зареждане на съобщенията на даден потребител - Димитър Годеманов
 * - приема текущия потребител id в системата и потребител id с който иска да си пише
 * - връща обект с message_type, content, send_date, file_path?, received (подредени по send_date DESC)
 * */
public interface LoadUserMessagesDao {
    List getGroupMessages(Long firstUserId, Long secondUserId );
}
