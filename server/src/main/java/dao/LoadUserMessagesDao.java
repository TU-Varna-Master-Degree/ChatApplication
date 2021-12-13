package dao;

import java.util.List;


public interface LoadUserMessagesDao {
    List getMessages(Long firstUserId, Long secondUserId );
}
