import domain.client.ServerRequest;
import domain.client.ServerResponse;
import domain.entities.User;
import domain.enums.StatusCode;
import service.UserService;
import service.impl.UserServiceImpl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

public class HandlerMapping {

    private final UserService userService;

    public HandlerMapping() {
        this.userService = new UserServiceImpl();
    }

    public ServerResponse map(Selector selector, SelectionKey key, ByteBuffer data) {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(data.array()))) {
            ServerRequest request = (ServerRequest) objectInputStream.readObject();
            ServerResponse serverResponse;

            switch (request.getOperationType()) {
                case USER_REGISTER:
                    serverResponse = userService.register((User) request.getData());
                    break;
                case USER_LOGIN:
                    serverResponse = userService.login((User) request.getData());
                    //TODO: Create session for auth users
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
}
