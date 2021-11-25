import config.HibernateConfiguration;
import dao.FriendshipDao;
import dao.UserDao;
import dao.impl.FriendshipDaoImpl;
import dao.impl.UserDaoImpl;
import domain.dto.FriendshipDto;
import domain.entities.User;

import java.util.List;

import static config.HibernateConfiguration.getEntityManager;

public class Main {

    private static final int CHAT_PORT = 1337;

    public static void main(String[] args) {
        HibernateConfiguration.init();

//        TestEchoServer server = new TestEchoServer(CHAT_PORT);
//        server.run();

        FriendshipDao friendshipDao = new FriendshipDaoImpl(getEntityManager());
        List<FriendshipDto> frindships = friendshipDao.getFrindships(1L);
        for (FriendshipDto frindship : frindships) {
            System.out.println(frindship.getReceiverUsername());
        }


        HibernateConfiguration.close();
    }
}
