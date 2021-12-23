import dao.FriendshipDao;
import dao.UserDao;
import dao.impl.FriendshipDaoImpl;
import dao.impl.UserDaoImpl;
import domain.client.dialogue.ServerRequest;
import domain.client.dialogue.ServerResponse;
import domain.client.dto.FindFriendDto;
import domain.client.dto.UpdateFriendshipDto;
import domain.client.dto.UserDto;
import domain.client.enums.StatusCode;
import org.modelmapper.ModelMapper;
import service.FriendshipService;
import service.SessionService;
import service.UserService;
import service.impl.FriendshipServiceImpl;
import service.impl.UserServiceImpl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.function.Supplier;

public class HandlerMapping {

    private final UserService userService;
    private final FriendshipService friendshipService;
    private final SessionService sessionService;

    public HandlerMapping(SessionService sessionService) {
        this.sessionService = sessionService;
        ModelMapper modelMapper = new ModelMapper();
        UserDao userDao = new UserDaoImpl();
        FriendshipDao friendshipDao = new FriendshipDaoImpl();
        this.userService = new UserServiceImpl(userDao, modelMapper);
        this.friendshipService = new FriendshipServiceImpl(friendshipDao);
    }

    public ServerResponse map(SocketChannel channel, SelectionKey key, ByteBuffer data) {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(data.array()))) {
            ServerRequest request = (ServerRequest) objectInputStream.readObject();
            Long userId = sessionService.getCurrentUserId(key);
            ServerResponse serverResponse;

            switch (request.getOperationType()) {
                case USER_REGISTER:
                    serverResponse = userService.register((UserDto) request.getData());
                    break;
                case USER_LOGIN:
                    serverResponse = userService.login((UserDto) request.getData());
                    sessionService.createSession(serverResponse, channel, key);
                    break;
                case FRIENDSHIP_LIST:
                    serverResponse = authorize(() ->
                            friendshipService.getFriendships(userId), key);
                    break;
                case CREATE_FRIENDSHIP:
                    serverResponse = authorize(() ->
                            friendshipService.createFriendship(userId, (Long) request.getData()), key);
                    break;
                case UPDATE_FRIENDSHIP:
                    serverResponse = authorize(() ->
                            friendshipService.updateFriendship(userId, (UpdateFriendshipDto) request.getData()), key);
                    break;
                case FIND_FRIENDS:
                    serverResponse = authorize(() ->
                            friendshipService.findFriends(userId, (String) request.getData()), key);
                    break;
                default:
                    serverResponse = new ServerResponse(StatusCode.FAILED, "Operation not found!");
                    break;
            }

            serverResponse.setOperationType(request.getOperationType());
            return serverResponse;
        } catch (IOException | ClassNotFoundException e) {
            return new ServerResponse(StatusCode.SERVER_EXCEPTION, "Something went wrong!");
        }
    }

    private ServerResponse authorize(Supplier<ServerResponse> action, SelectionKey key) {
        if (!sessionService.isAuthorized(key)) {
            return new ServerResponse(StatusCode.FAILED, "Please, authenticate first!");
        } else {
            return action.get();
        }
    }
}
