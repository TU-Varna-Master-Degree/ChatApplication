package dao;

import domain.enums.FriendshipState;

public interface BefriendingResolveDao{
    public void BefriendingResolve(long befriending, long befriended, FriendshipState state);
}
