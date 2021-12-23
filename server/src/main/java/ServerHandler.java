import domain.client.dialogue.ServerRequest;
import domain.client.dialogue.ServerResponse;
import domain.client.dto.FindFriendDto;
import domain.client.dto.UpdateFriendshipDto;
import domain.client.dto.UserDto;
import domain.client.enums.FriendshipState;
import domain.client.enums.OperationType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerHandler {

    private final static Logger logger = LogManager.getLogger();
    private static ExecutorService executorService;
    private static SocketChannel server;
    private static ServerHandler serverHandler;

    public static void main(String[] args) {
        serverHandler = new ServerHandler();
        serverHandler.start();

        // ** Register request ** //
//        ServerRequest<UserDto> request2 = new ServerRequest<>(OperationType.USER_REGISTER);
//        UserDto user = new UserDto();
//        user.setUsername("Tihomir");
//        user.setEmail("tisho@abv.bg");
//        user.setPassword("1111");
//        request2.setData(user);

        // ** Login request ** //
        ServerRequest<UserDto> request1 = new ServerRequest<>(OperationType.USER_LOGIN);
        UserDto user = new UserDto();
        user.setEmail("gosho44@abv.bg");
        user.setPassword("1111");
        request1.setData(user);

        serverHandler.sendRequest(request1);

        // ** Get friendships ** //
//        ServerRequest<Long> request2 = new ServerRequest<>(OperationType.FRIENDSHIP_LIST);

        // ** Create friendship ** //
//        ServerRequest<Long> request2 = new ServerRequest<>(OperationType.CREATE_FRIENDSHIP);
//        Long receiverId = 9L;
//        request2.setData(receiverId);

        // ** Update friendship state ** //
//        ServerRequest<UpdateFriendshipDto> request2 = new ServerRequest<>(OperationType.UPDATE_FRIENDSHIP);
//        UpdateFriendshipDto updateFriendshipDto = new UpdateFriendshipDto();
//        Long receiverId = 8L;
//        updateFriendshipDto.setReceiverId(receiverId);
//        updateFriendshipDto.setFriendshipState(FriendshipState.ACCEPTED);
//        request2.setData(updateFriendshipDto);

        // ** Find friends ** //
        ServerRequest<String> request2 = new ServerRequest<>(OperationType.FIND_FRIENDS);
        String username = "o";
        request2.setData(username);

        new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            serverHandler.sendRequest(request2);
        }).start();
    }

    public void start() {
        try {
            server = SocketChannel.open(new InetSocketAddress(1300));
        } catch (IOException e) {
            logger.error("Unable to connect with server!");
            System.exit(1);
        }
        executorService = Executors.newFixedThreadPool(2);
        executorService.execute(this::listen);
    }

    public void stop() {
        executorService.shutdown();
        try {
            server.close();
        } catch (IOException e) {
            logger.error("Unable to close connection with server!");
        }
    }

    public void sendRequest(ServerRequest serverRequest) {
        executorService.execute(() -> send(serverRequest));
    }

    private void send(ServerRequest serverRequest) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
            objectOutputStream.writeObject(serverRequest);
            objectOutputStream.flush();
            server.write(ByteBuffer.wrap(byteArrayOutputStream.toByteArray()));
        } catch (IOException e) {
            logger.error("Sending message to the server failed!");
        }
    }

    private void listen() {
        while (true) {
            ByteBuffer data = ByteBuffer.allocate(1000 * 16);
            try {
                server.read(data);
            } catch (IOException e) {
                logger.error("Unable to read data from server!");
            }

            try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data.array());
                 ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {
                ServerResponse response = (ServerResponse) objectInputStream.readObject();
                System.out.println(response.getCode());
                System.out.println(response.getMessage());
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            data.clear();

            //TODO: remove after test
            if (++requestCounter == 2) {
                serverHandler.stop();
                break;
            }
        }
    }

    private static int requestCounter = 0; //TODO: remove after test
}
