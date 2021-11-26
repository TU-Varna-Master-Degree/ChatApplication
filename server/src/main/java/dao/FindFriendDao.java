package dao;

import java.util.List;

/**
 * 11. Търсене на нов приятел - Димитър Годеманов
 * - приема имe на потребител
 * - трябва да връща единствено потребители, които не са приятели с него
 * - връща username и id на потребителите
 * **/
public interface FindFriendDao {
    List findFriendByName(Long userId, String username);
}
