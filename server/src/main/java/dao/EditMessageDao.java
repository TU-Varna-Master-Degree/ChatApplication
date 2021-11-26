package dao;

/**
 * 14. Редактиране на съобщение (дали даден потебител го е видял) - Димитър Годеманов
 *  - приема notification id
 *  - редактира полето received и връща void
 * **/
public interface EditMessageDao {
    void markAsReceived(Long notificationId);
}
