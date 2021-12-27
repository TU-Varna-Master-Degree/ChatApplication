package init;

import dao.FriendshipDao;
import dao.GroupDao;
import dao.MessageDao;
import dao.UserDao;
import dao.impl.FriendshipDaoImpl;
import dao.impl.GroupDaoImpl;
import dao.impl.MessageDaoImpl;
import dao.impl.UserDaoImpl;
import domain.client.dialogue.ServerRequest;
import domain.client.dialogue.ServerResponse;
import domain.client.dto.AddGroupFriendsDto;
import domain.client.dto.SendMessageDto;
import domain.client.dto.UpdateFriendshipDto;
import domain.client.dto.UserDto;
import domain.client.enums.StatusCode;
import org.modelmapper.ModelMapper;
import service.*;
import service.impl.FriendshipServiceImpl;
import service.impl.GroupServiceImpl;
import service.impl.NotificationServiceImpl;
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
    private final GroupService groupService;
    private final NotificationService notificationService;
    private final SessionService sessionService;

    public HandlerMapping(SessionService sessionService) {
        this.sessionService = sessionService;
        ModelMapper modelMapper = new ModelMapper();
        UserDao userDao = new UserDaoImpl();
        FriendshipDao friendshipDao = new FriendshipDaoImpl();
        GroupDao groupDao = new GroupDaoImpl();
        MessageDao messageDao = new MessageDaoImpl();
        this.userService = new UserServiceImpl(userDao, modelMapper);
        this.groupService = new GroupServiceImpl(groupDao, userDao);
        this.friendshipService = new FriendshipServiceImpl(friendshipDao, groupService);
        this.notificationService = new NotificationServiceImpl(messageDao, groupDao, userDao, sessionService);
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
                case USER_GROUPS:
                    serverResponse = authorize(() ->
                            groupService.getUserGroups(userId), key);
                    break;
                case GROUP_FRIENDS_LIST:
                    serverResponse = authorize(() ->
                            groupService.getGroupFriends(userId, (Long) request.getData()), key);
                    break;
                case ADD_GROUP_FRIENDS:
                    serverResponse = authorize(() ->
                            groupService.addUsersToGroup(userId, (AddGroupFriendsDto) request.getData()), key);
                    break;
                case GROUP_NOTIFICATIONS:
                    serverResponse = authorize(() ->
                            notificationService.getGroupNotifications(userId, (Long) request.getData()), key);
                    break;
                case CREATE_NOTIFICATION:
                    serverResponse = authorize(() ->
                            notificationService.createMessage(userId, (SendMessageDto) request.getData()), key);
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
