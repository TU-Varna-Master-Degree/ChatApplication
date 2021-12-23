package dao.impl;

import dao.LoadUserGroupsDao;
import domain.client.dto.GroupsDto;

import javax.persistence.EntityManager;
import java.util.List;

public class LoadUserGroupsDaoImpl implements LoadUserGroupsDao {
    private static final String QUERY_PARAM_USER_ID = "userId";
    private static final String SUBQUERY_DATE =
            "SELECT notif.id.notification.sendDate" +
            "FROM GroupNotifications notif " +
            "WHERE notif.id.group.id=:" + QUERY_PARAM_USER_ID;

    // TODO: Fix problem with select sub-query ????
    // Programaticall sorting
    private static final String QUERY_STRING =
            "SELECT new domain.client.dto.GroupsDto( " +
                        "userGroup.id.group.id, " +
                        "userGroup.id.group.name, " +
                        "userGroup.joinDate ), (" +  SUBQUERY_DATE + ") as mostRecentDate " +
            "FROM UserGroup userGroup " +
            "WHERE userGroup.id.user.id=:" + QUERY_PARAM_USER_ID + " " +
            "GROUP BY MAX(mostRecentDate) " +
            "ORDER BY mostRecentDate DESC";

    private final EntityManager entityManager;

    public LoadUserGroupsDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<GroupsDto> getGroups(Long userId) {
        return entityManager.createQuery( QUERY_STRING )
                .setParameter(QUERY_PARAM_USER_ID, userId)
                .getResultList();
    }
}
