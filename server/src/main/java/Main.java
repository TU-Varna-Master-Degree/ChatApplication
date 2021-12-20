import config.HibernateConfiguration;
import dao.*;
import dao.impl.*;
import domain.dto.FindFriendDto;
import domain.dto.FriendsToGroupDto;
import domain.dto.UserMessagesDto;
import domain.dto.WhatGroupDto;
import domain.enums.FriendshipState;

import java.util.List;

import static config.HibernateConfiguration.getEntityManager;

public class Main {

    public static void main(String[] args) {
        HibernateConfiguration.init();
        DispatcherServlet.listen();

        DispatcherServlet.close();
        HibernateConfiguration.close();
    }

    public static void WhatGroupById(long Id) {
        WhatGroupDao whatGroupDao = new WhatGroupDaoImpl(getEntityManager());
        List<domain.dto.WhatGroupDto> whatGroups = whatGroupDao.WhatGroup(Id);
        for (WhatGroupDto gr : whatGroups)
            System.out.println("\n име на група по ID : " + gr.getGroupName() +
                    "Date " + gr.getFoundation() + "\n");
    }


    public static void WhatFriendsCanJoinGroup(long id) {
        FriendsToGroupDao friendsToGroupDao =
                new FriendsToGroupDaoImpl(getEntityManager());
        List<FriendsToGroupDto> friends = friendsToGroupDao.FriendsForGroup(id);
        System.out.println("Приятели, които могат да бъдат поканени в група: ");
        for (FriendsToGroupDto fr : friends) {
            System.out.println(fr.getSenderUsername() + "    " + fr.getReceiverUsername() +
                    "    " + fr.getState());
        }
    }

        public static void CreateAGroup( long Id, String name){
            CreateGroupDao createGroupDao = new CreateGroupDaoImpl(getEntityManager());
            boolean isUnique = createGroupDao.CreateGroup(Id, name);
            System.out.println(isUnique);
        }

        public static void SetFriendshipState(long id1, long id2, FriendshipState state){

    BefriendingResolveDao befriendingResolveDao =
            new BefriendingResolveDaoImpl(getEntityManager());
        befriendingResolveDao.BefriendingResolve(
                id1, id2, state);
    }

public static void NewFriendshipPending(long id1,long id2){
    BefriendingAskDao befriendingAskDao =
            new BefriendingAskDaoImpl(getEntityManager());
        befriendingAskDao.BefriendingAsk(id1, id2);
    }


    public static void AddAFriendToGroup(long id1,long id2Group) {
        AddToGroupDao addToGroupDao = new AddToGroupDaoImpl(getEntityManager());
        addToGroupDao.AddToGroup(id1, id2Group);
    }

    static void findFriendTest() {
        try {
            dao.FindFriendDao findFriend = new dao.impl.FindFriendDaoImpl(getEntityManager());

            List<FindFriendDto> l = findFriend.findFriendByName(6L, "Azure");
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    static void loadUserMessagesTest() {
        dao.LoadUserMessagesDao obj = new dao.impl.LoadUserMessagesDaoImpl(getEntityManager());
        try {
            // TODO: Fix Query, Test should return convo of user 1L and user 2L
            List<domain.dto.UserMessagesDto> l = obj.getMessages(1L, 2L);
            for (UserMessagesDto o : l) {

            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }

    }

    static void loadUserGroupsTest() {
        dao.LoadUserGroupsDao obj = new LoadUserGroupsDaoImpl(getEntityManager());
        try {
            List l = obj.getGroups(1L);

        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }


}
