package dao.impl;

import dao.FriendsToGroupDao;
import domain.client.dto.FriendsToGroupDto;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

import static domain.client.enums.FriendshipState.ACCEPTED;

//връща списък с приятелите с потвърден статус ,
      // които могат да бъдат поканени в група

public class FriendsToGroupDaoImpl implements FriendsToGroupDao {
    public EntityManager entityManager;

    public FriendsToGroupDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    public List<FriendsToGroupDto> FriendsForGroup(long id){
        String string ="SELECT new domain.client.dto.FriendsToGroupDto" +
                "(friends.id.receiver.username, " +
                " friends.id.sender.username,friends.friendshipState) " +
                " FROM Friendship friends " +
                " WHERE (friends.id.sender.id = :user OR friends.id.receiver.id = :user)" +
                " AND friends.friendshipState = :state ";
        Query query=entityManager.createQuery(string);
        query.setParameter("user",id);
        query.setParameter("state",ACCEPTED);
        List<FriendsToGroupDto> list= query.getResultList();

        return  list;

    }

}
