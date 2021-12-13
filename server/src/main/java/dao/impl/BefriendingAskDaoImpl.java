package dao.impl;

import dao.BefriendingAskDao;
import domain.entities.Friendship;
import domain.entities.FriendshipId;
import domain.entities.User;
import domain.enums.FriendshipState;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class BefriendingAskDaoImpl implements BefriendingAskDao {
    private EntityManager entityManager;

    public BefriendingAskDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    public void BefriendingAsk(long befriending, long befriended){
        String check="SELECT count (friendshipState) from Friendship friend where " +
                " (friend.id.sender.id=:sender or friend.id.sender.id=:receiver) and " +
                "(friend.id.receiver.id=:sender or friend.id.receiver.id=:receiver)";
        Query query=entityManager.createQuery(check);
        query.setParameter("sender",befriending);
        query.setParameter("receiver",befriended);
        if ( (long)query.getSingleResult()==0) {

            FriendshipId friendshipId = new FriendshipId();
            friendshipId.setSender((User) entityManager.
                    createQuery("Select " +
                            "user from User user Where user.id = : senderId").
                    setParameter("senderId", befriending).
                    getSingleResult());
            friendshipId.setReceiver((User) entityManager.createQuery("Select " +
                            "user from User user Where user.id = : receiverId").
                    setParameter("receiverId", befriended).getSingleResult());
            System.out.println(friendshipId.getReceiver().getUsername());

            Friendship friendship = new Friendship();
            friendship.setId(friendshipId);
            friendship.setFriendshipState(FriendshipState.PENDING);
            System.out.println(friendship.getFriendshipState());


            entityManager.getTransaction().begin();
            entityManager.persist((Friendship) friendship);
            entityManager.getTransaction().commit();

        }
        else System.out.println("Вече има създадено такова приятелство");

    }

}
