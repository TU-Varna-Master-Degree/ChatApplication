package dao.impl;

import config.HibernateConfiguration;
import dao.FriendshipDao;
import domain.client.dto.FindFriendDto;
import domain.client.dto.FriendshipDto;
import domain.client.enums.FriendshipState;
import domain.entities.Friendship;
import domain.entities.FriendshipId;
import domain.entities.User;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.List;

public class FriendshipDaoImpl implements FriendshipDao {

    private final EntityManager entityManager;

    public FriendshipDaoImpl() {
        this.entityManager = HibernateConfiguration.getEntityManager();
    }

    @Override
    public List<FriendshipDto> getFriendships(Long userId) {
        Query query = entityManager.createQuery(
        "SELECT new domain.client.dto.FriendshipDto(( " +
                "    SELECT iug.id.group.id" +
                "     FROM UserGroup iug" +
                "    WHERE iug.id.group.parent IS NULL" +
                "     AND iug.id.user.id = f.id.receiver.id" +
                "     AND EXISTS ( " +
                "       SELECT 1 " +
                "        FROM UserGroup iiug" +
                "       WHERE iiug.id.group.parent IS NULL" +
                "        AND iug.id.group.id = iiug.id.group.id " +
                "        AND iiug.id.user.id = f.id.sender.id" +
                "     )" +
                "   ), (case when f.id.receiver.id = :userId then f.id.sender.id else f.id.receiver.id end), " +
                " (case when f.id.receiver.id = :userId then f.id.sender.username else f.id.receiver.username end), " +
                " f.friendshipState) " +
                " FROM Friendship f " +
                " WHERE ((f.id.sender.id = :userId AND f.friendshipState = 'ACCEPTED') OR f.id.receiver.id = :userId)" +
                " AND f.friendshipState != :friendshipState " +
                " ORDER BY f.friendshipState DESC, f.updatedStateDate DESC");

        query.setParameter("userId", userId);
        query.setParameter("friendshipState", FriendshipState.REJECTED);

        return query.getResultList();
    }

    @Override
    public Friendship getFriendship(long senderId, long receiverId) {
        String hql = "SELECT f from Friendship f where " +
                " (f.id.sender.id=:sender and f.id.receiver.id=:receiver) or " +
                " (f.id.receiver.id=:sender and f.id.sender.id=:receiver)";

        Query query = entityManager.createQuery(hql);
        query.setParameter("sender", senderId);
        query.setParameter("receiver", receiverId);

        try {
            return (Friendship) query.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public void createFriendship(long senderId, long receiverId) {
        User sender = entityManager.find(User.class, senderId);
        User receiver = entityManager.find(User.class, receiverId);

        FriendshipId friendshipId = new FriendshipId();
        friendshipId.setSender(sender);
        friendshipId.setReceiver(receiver);

        Friendship friendship = new Friendship();
        friendship.setId(friendshipId);
        friendship.setFriendshipState(FriendshipState.PENDING);
        friendship.setUpdatedStateDate(LocalDateTime.now());

        entityManager.getTransaction().begin();
        entityManager.persist(friendship);
        entityManager.getTransaction().commit();
    }

    @Override
    public void changeFriendshipState(Friendship friendship, FriendshipState newState) {
        friendship.setFriendshipState(newState);
        friendship.setUpdatedStateDate(LocalDateTime.now());

        entityManager.getTransaction().begin();
        entityManager.persist(friendship);
        entityManager.getTransaction().commit();
    }

    @Override
    public List<FindFriendDto> findFriendByName(Long userId, String username) {
        String hql = "SELECT new domain.client.dto.FindFriendDto(user.id, user.username) " +
                "FROM User AS user " +
                "WHERE (lower(user.username) LIKE lower(:username)) " +
                " AND user.id != :userId" +
                " AND NOT EXISTS (SELECT 1 FROM Friendship f" +
                "    WHERE (f.id.sender.id = user.id AND f.id.receiver.id = :userId) OR " +
                "     (f.id.sender.id = :userId AND f.id.receiver.id = user.id))";

        Query query = entityManager.createQuery(hql);
        query.setParameter("username", String.format("%%%s%%", username));
        query.setParameter("userId", userId);
        return query.getResultList();
    }
}
