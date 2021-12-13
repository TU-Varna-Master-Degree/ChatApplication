package dao;

import java.util.List;


public interface LoadUserGroupsDao {
    List getGroups(Long userId);
}
