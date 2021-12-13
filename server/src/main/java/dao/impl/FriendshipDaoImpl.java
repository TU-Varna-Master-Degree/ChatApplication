package dao.impl;

import dao.FriendshipDao;
import domain.dto.FriendshipDto;
import domain.enums.FriendshipState;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class FriendshipDaoImpl implements FriendshipDao {

    private final EntityManager entityManager;

    public FriendshipDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<FriendshipDto> getFriendships(Long userId) {
        Query query = entityManager.createQuery(
        "SELECT new domain.dto.FriendshipDto(f.id.receiver.username, " +
                " f.id.sender.username, " +
                " f.friendshipState) " +
                " FROM Friendship f " +
                " WHERE (f.id.sender.id = :userId OR f.id.receiver.id = :userId)" +
                " AND f.friendshipState != :friendshipState " +
                " ORDER BY f.friendshipState DESC");

        query.setParameter("userId", userId);
        query.setParameter("friendshipState", FriendshipState.REJECTED);


        return query.getResultList();
    }
}
