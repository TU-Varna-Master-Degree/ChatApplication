package dao.impl;

import dao.BefriendingResolveDao;
import domain.enums.FriendshipState;

import javax.persistence.EntityManager;

    public class BefriendingResolveDaoImpl implements BefriendingResolveDao {
        private EntityManager entityManager;

        public BefriendingResolveDaoImpl(EntityManager entityManager) {
            this.entityManager = entityManager;
        }

        public void BefriendingResolve(EntityManager entityManager) {
            this.entityManager = entityManager;
        }
        public void BefriendingResolve (long befriending, long befriended,
                                           FriendshipState state){
            String query="Update Friendship fr set fr.friendshipState = : newState where " +
                    "(fr.id.sender.id=:userA or fr.id.receiver.id= :userA)and " +
                    "(fr.id.sender.id=:userB or fr.id.receiver.id= :userB)";
            entityManager.getTransaction().begin();
            entityManager.createQuery(query).
                    setParameter("newState",state).
                    setParameter("userA",befriending).
                    setParameter("userB",befriended)
                    .executeUpdate();
            entityManager.getTransaction().commit();

        }


    }



