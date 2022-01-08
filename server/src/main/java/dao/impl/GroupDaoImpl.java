package dao.impl;

import config.HibernateConfiguration;
import dao.GroupDao;
import domain.client.dto.GroupFriendDto;
import domain.client.dto.GroupUserDto;
import domain.enums.FriendshipState;
import domain.entities.Group;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class GroupDaoImpl implements GroupDao {

    private final EntityManager entityManager;

    public GroupDaoImpl() {
        this.entityManager = HibernateConfiguration.getEntityManager();
    }

    public List<Object[]> getUserGroups(Long userId) {
        String sql = "SELECT og," +
                "   (SELECT MAX(n.sendDate) FROM og.notifications n) as lastSendMessageDate" +
                " FROM Group og " +
                " WHERE og.id in " +
                "  (SELECT g2.id.group.id FROM UserGroup g1 " +
                "    JOIN UserGroup g2 ON g1.id.group.id = g2.id.group.id " +
                "     WHERE g1.id.user.id = :userId" +
                "    GROUP BY g2.id.group.id) " +
                "  AND ((SIZE(og.userGroups) = 2 AND SIZE(og.notifications) > 0) " +
                "    OR SIZE(og.userGroups) > 2) " +
                "ORDER BY lastSendMessageDate DESC";

        Query query = entityManager.createQuery(sql);
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    public List<GroupFriendDto> getGroupFriends(Long userId, Long groupId) {
        String sql = "SELECT new domain.client.dto.GroupFriendDto(" +
                " (case when f.id.receiver.id = :userId then f.id.sender.id else f.id.receiver.id end)," +
                " (case when f.id.receiver.id = :userId then f.id.sender.username else f.id.receiver.username end)) " +
                " FROM Friendship f " +
                "WHERE (f.id.receiver.id = :userId" +
                "  OR f.id.sender.id = :userId)" +
                " AND f.friendshipState = :friendshipState" +
                " AND (case when f.id.receiver.id = :userId then f.id.sender.id else f.id.receiver.id end) NOT IN " +
                "   (SELECT ug.id.user.id " +
                "     FROM UserGroup ug " +
                "    WHERE ug.id.group.id = :groupId)" +
                " ORDER BY f.updatedStateDate DESC";

        Query query = entityManager.createQuery(sql);
        query.setParameter("userId", userId);
        query.setParameter("groupId", groupId);
        query.setParameter("friendshipState", FriendshipState.ACCEPTED);
        return query.getResultList();
    }

    public void save(Group group) {
        entityManager.getTransaction().begin();
        entityManager.persist(group);
        entityManager.getTransaction().commit();
    }

    public Group getById(Long groupId) {
        return entityManager.find(Group.class, groupId);
    }

    public boolean isUserParticipateInGroup(Long userId, Long groupId) {
        String sql = "SELECT count(ug.id.user.id)" +
                "FROM UserGroup ug " +
                "WHERE ug.id.group.id = :groupId " +
                " AND ug.id.user.id = :userId";

        Query query = entityManager.createQuery(sql);
        query.setParameter("userId", userId);
        query.setParameter("groupId", groupId);
        Long userCount = (Long) query.getSingleResult();
        return userCount != 0;
    }

    public List<GroupUserDto> getParticipantsInGroup(Long groupId) {
        String sql = "SELECT new domain.client.dto.GroupUserDto( " +
                "  ug.id.user.id," +
                "  ug.id.user.username," +
                "  ug.joinDate)" +
                "FROM UserGroup ug " +
                "WHERE ug.id.group.id = :groupId " +
                "ORDER BY ug.joinDate DESC ";

        Query query = entityManager.createQuery(sql);
        query.setParameter("groupId", groupId);
        return query.getResultList();
    }
}
