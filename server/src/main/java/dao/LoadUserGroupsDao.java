package dao;

import java.util.List;

/**
 * 7. Зареждане на групите в които е добавен потребител - Димитър Годеманов
 * - приема потребител id
 * - връща група id, името на групата, кога се е присъединил (подредени по последното съобщение от всяка група по обратен ред Notification(send_date))
 * */
public interface LoadUserGroupsDao {
    List getGroups(Long groupId);
}
