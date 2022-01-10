package init;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import domain.enums.StatusCode;
import service.*;
import service.impl.FriendshipServiceImpl;
import service.impl.GroupServiceImpl;
import service.impl.NotificationServiceImpl;
import service.impl.UserServiceImpl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.util.function.Supplier;

public class HandlerMapping {

    private final UserService userService;
    private final FriendshipService friendshipService;
    private final GroupService groupService;
    private final NotificationService notificationService;
    private final SessionService sessionService;
    private final ObjectMapper objectMapper;

    public HandlerMapping(SessionService sessionService, ObjectMapper objectMapper) {
        this.sessionService = sessionService;
        this.objectMapper = objectMapper;
        UserDao userDao = new UserDaoImpl();
        FriendshipDao friendshipDao = new FriendshipDaoImpl();
        GroupDao groupDao = new GroupDaoImpl();
        MessageDao messageDao = new MessageDaoImpl();
        this.userService = new UserServiceImpl(userDao);
        this.groupService = new GroupServiceImpl(groupDao, userDao, sessionService);
        this.friendshipService = new FriendshipServiceImpl(friendshipDao, groupService);
        this.notificationService = new NotificationServiceImpl(messageDao, groupDao, userDao, sessionService);
    }

    public ServerResponse map(SelectionKey key, ByteBuffer data) {
        try {
            ServerRequest request = objectMapper.readValue(data.array(), ServerRequest.class);
            Long userId = sessionService.getCurrentUserId(key);
            ServerResponse serverResponse;

            switch (request.getOperationType()) {
                case USER_REGISTER:
                    serverResponse = userService.register(getData(request, UserDto.class));
                    break;
                case USER_LOGIN:
                    serverResponse = userService.login(getData(request, UserDto.class));
                    sessionService.createSession(serverResponse, key);
                    break;
                case FRIENDSHIP_LIST:
                    serverResponse = authorize(() ->
                            friendshipService.getFriendships(userId), key);
                    break;
                case CREATE_FRIENDSHIP:
                    serverResponse = authorize(() ->
                            friendshipService.createFriendship(userId, getData(request, Long.class)), key);
                    break;
                case UPDATE_FRIENDSHIP:
                    serverResponse = authorize(() ->
                            friendshipService.updateFriendship(userId, getData(request, UpdateFriendshipDto.class)), key);
                    break;
                case FIND_FRIENDS:
                    serverResponse = authorize(() ->
                            friendshipService.findFriends(userId, getData(request, String.class)), key);
                    break;
                case USER_GROUPS:
                    serverResponse = authorize(() ->
                            groupService.getUserGroups(userId), key);
                    break;
                case GROUP_FRIENDS_LIST:
                    serverResponse = authorize(() ->
                            groupService.getGroupFriends(userId, getData(request, Long.class)), key);
                    break;
                case ADD_GROUP_FRIENDS:
                    serverResponse = authorize(() ->
                            groupService.addUsersToGroup(userId, getData(request, AddGroupFriendsDto.class)), key);
                    break;
                case GROUP_NOTIFICATIONS:
                    serverResponse = authorize(() ->
                            notificationService.getGroupNotifications(userId, getData(request, Long.class)), key);
                    break;
                case CREATE_NOTIFICATION:
                    serverResponse = authorize(() ->
                            notificationService.createMessage(userId, getData(request, SendMessageDto.class)), key);
                    break;
                case EDIT_NOTIFICATION:
                    serverResponse = authorize(() ->
                            notificationService.editMessage(userId, getData(request, SendMessageDto.class)), key);
                    break;
                case GET_NOTIFICATION:
                    serverResponse = authorize(() ->
                            notificationService.getMessage(userId, getData(request, Long.class)), key);
                    break;
                default:
                    serverResponse = new ServerResponse(StatusCode.FAILED, "Operation not found!");
                    break;
            }

            serverResponse.setOperationType(request.getOperationType());
            return serverResponse;
        } catch (IOException e) {
            return new ServerResponse(StatusCode.SERVER_EXCEPTION, "Something went wrong!");
        }
    }

    private <T> T getData(ServerRequest responseDate, Class<T> tClass) {
        return objectMapper.convertValue(responseDate.getData(), tClass);
    }

    private ServerResponse authorize(Supplier<ServerResponse> action, SelectionKey key) {
        if (!sessionService.isAuthorized(key)) {
            return new ServerResponse(StatusCode.FAILED, "Please, authenticate first!");
        } else {
            return action.get();
        }
    }
}
