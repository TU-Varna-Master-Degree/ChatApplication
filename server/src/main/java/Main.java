import config.HibernateConfiguration;
import dao.*;
import dao.impl.*;
import domain.dto.FriendsToGroupDto;
import domain.dto.WhatGroupDto;
import domain.enums.FriendshipState;

import java.util.List;

import static config.HibernateConfiguration.getEntityManager;

public class Main {

    // private static final int CHAT_PORT = 1337;


    public static void main(String[] args) {
        HibernateConfiguration.init();

       // WhatGroupById(2L);
        // CheckLogin("liliana223@abv.bg","TGhjM2");
        // WhatFriendsCanJoinGroup(3L);
         //CreateAGroup(8,"Many People");
        //SetFriendshipState(2,5,FriendshipState.ACCEPTED);
        //NewFriendshipPending(3,7);
        //AddAFriendToGroup(2,9);

        HibernateConfiguration.close();
    }

    public static void WhatGroupById(long Id) {
        WhatGroupDao whatGroupDao = new WhatGroupDaoImpl(getEntityManager());
        List<domain.dto.WhatGroupDto> whatGroups = whatGroupDao.WhatGroup(Id);
        for (WhatGroupDto gr : whatGroups)
            System.out.println("\n име на група по ID : " + gr.getGroupName() +
                    "Date " + gr.getFoundation() + "\n");
    }

    public static void CheckLogin(String email, String pass) {
        LoginUserDao loginUserDao = new LoginUserDaoImpl(getEntityManager());
        boolean isCorrect = loginUserDao.Login(email,
                pass);
        System.out.println("\n The credentials of the user are :  " + isCorrect + "\n");


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


}











