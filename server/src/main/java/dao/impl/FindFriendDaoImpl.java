package dao.impl;

import dao.FindFriendDao;
import domain.dto.FindFriendDto;

import javax.persistence.EntityManager;
import java.util.List;

public class FindFriendDaoImpl implements FindFriendDao {
    private final static String QUERY_PARAM_USER_ID = ":userID";
    private final static String QUERY_PARAM_USERNAME = ":username";
    private final static String QUERY_STRING =
            "SELECT new domain.dto.FindFriendDto(user.id, user.username) " +
            "FROM User AS user " +
            "WHERE (user.username LIKE " + QUERY_PARAM_USERNAME + ") AND " +
                    "( (SELECT Friendship AS f " +
                          "WHERE " +
                              "(f.id.sender.id !=" + QUERY_PARAM_USER_ID + ") OR " +
                              "(f.id.sender.id !=" + QUERY_PARAM_USER_ID + ")) IS NULL)";

    private final EntityManager entityManager;

    public FindFriendDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<FindFriendDto> findFriendByName(Long userId, String username) {
        return entityManager.createQuery(QUERY_STRING)
                .setParameter( QUERY_PARAM_USERNAME, username)
                .getResultList();
    }

}
