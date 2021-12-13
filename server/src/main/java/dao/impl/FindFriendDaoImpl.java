package dao.impl;

import dao.FindFriendDao;
import domain.dto.FindFriendDto;

import javax.persistence.EntityManager;
import java.util.List;

public class FindFriendDaoImpl implements FindFriendDao {
    private final static String QUERY_PARAM_USER_ID = "userID";
    private final static String QUERY_PARAM_USERNAME = "username";
    private final static String QUERY_STRING =
            "SELECT new domain.dto.FindFriendDto(user.id, user.username) " +
            "FROM User AS user " +
            "WHERE (user.username LIKE :" + QUERY_PARAM_USERNAME + ") AND " +
                    "(SELECT COUNT(f) as count_result FROM Friendship f WHERE " +
                    "((f.id.sender.id = user.id) AND (f.id.receiver.id = :userID)) OR " +
                    "((f.id.sender.id = :userID) AND (f.id.receiver.id = user.id)))  = 0";

    private final EntityManager entityManager;

    public FindFriendDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<FindFriendDto> findFriendByName(Long userId, String username) {
        return entityManager.createQuery(QUERY_STRING)
                .setParameter( QUERY_PARAM_USERNAME, username)
                .setParameter( QUERY_PARAM_USER_ID, userId )
                .getResultList();
    }

}
