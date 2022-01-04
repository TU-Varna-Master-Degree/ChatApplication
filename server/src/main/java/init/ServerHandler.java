package init;

import domain.client.dialogue.ServerRequest;
import domain.client.dialogue.ServerResponse;
import domain.client.dto.UserDto;
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
//        user.setUsername("Gosho32");
//        user.setEmail("gosho32@abv.bg");
//        user.setPassword("1111");
//        request2.setData(user);

        // ** Login request ** //
        ServerRequest<UserDto> request1 = new ServerRequest<>(OperationType.USER_LOGIN);
        UserDto user = new UserDto();
        user.setUsername("Gosho");
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
//        Long receiverId = 1L;
//        updateFriendshipDto.setReceiverId(receiverId);
//        updateFriendshipDto.setFriendshipState(FriendshipState.ACCEPTED);
//        request2.setData(updateFriendshipDto);

        // ** Find friends ** //
//        ServerRequest<String> request2 = new ServerRequest<>(OperationType.FIND_FRIENDS);
//        String username = "o";
//        request2.setData(username);

        // ** Get user groups **//
//        ServerRequest<String> request2 = new ServerRequest<>(OperationType.USER_GROUPS);

        // ** Get friends for group ** //
//        ServerRequest<Long> request2 = new ServerRequest<>(OperationType.GROUP_FRIENDS_LIST);
//        Long groupId = 2L;
//        request2.setData(groupId);

        // ** Update group participants ** //
//        ServerRequest<AddGroupFriendsDto> request2 = new ServerRequest<>(OperationType.ADD_GROUP_FRIENDS);
//        AddGroupFriendsDto addGroupFriendsDto = new AddGroupFriendsDto();
//        addGroupFriendsDto.setGroupId(3L);
//        addGroupFriendsDto.setUserIds(Arrays.asList(2L, 4L));
//        request2.setData(addGroupFriendsDto);

        // ** Get group notifications ** //
        ServerRequest<Long> request2 = new ServerRequest<>(OperationType.GROUP_NOTIFICATIONS);
        Long groupId = 7L;
        request2.setData(groupId);

        // ** Send message - TEXT ** //
//        ServerRequest<SendMessageDto> request2 = new ServerRequest<>(OperationType.CREATE_NOTIFICATION);
//        SendMessageDto sendMessageDto = new SendMessageDto();
//        sendMessageDto.setGroupId(7L);
//        sendMessageDto.setMessageType(MessageType.TEXT);
//        sendMessageDto.setContent("Test 10");
//        request2.setData(sendMessageDto);

        // ** Send message - FILE ** //
//        ServerRequest<SendMessageDto> request2 = new ServerRequest<>(OperationType.CREATE_NOTIFICATION);
//        SendMessageDto sendMessageDto = new SendMessageDto();
//        sendMessageDto.setGroupId(7L);
//        sendMessageDto.setMessageType(MessageType.FILE);
//        sendMessageDto.setFileName("tree");
//        sendMessageDto.setFileType("png");
//        final String filePath = "C:\\Users\\Ivaylo_nikolaev\\Desktop\\ChatApplication\\server\\src\\main\\resources\\files\\tree.png";
//        try {
//            sendMessageDto.setFile(Files.readAllBytes(Paths.get(filePath)));
//        } catch (IOException e) {
//            System.out.println("Parsing file exception.");
//        }
//        request2.setData(sendMessageDto);

        // ** Edit message - TEXT ** //
//        ServerRequest<SendMessageDto> request2 = new ServerRequest<>(OperationType.EDIT_NOTIFICATION);
//        SendMessageDto sendMessageDto = new SendMessageDto();
//        sendMessageDto.setMessageId(21L);
//        sendMessageDto.setMessageType(MessageType.TEXT);
//        sendMessageDto.setContent("Test 9");
//        request2.setData(sendMessageDto);

        // ** Edit message - FILE ** //
//        ServerRequest<SendMessageDto> request2 = new ServerRequest<>(OperationType.EDIT_NOTIFICATION);
//        SendMessageDto sendMessageDto = new SendMessageDto();
//        sendMessageDto.setMessageId(21L);
//        sendMessageDto.setMessageType(MessageType.FILE);
//        sendMessageDto.setFileName("tree");
//        sendMessageDto.setFileType("png");
//        final String filePath = "C:\\Users\\Ivaylo_nikolaev\\Desktop\\ChatApplication\\server\\src\\main\\resources\\files\\tree.png";
//        try {
//            sendMessageDto.setFile(Files.readAllBytes(Paths.get(filePath)));
//        } catch (IOException e) {
//            System.out.println("Parsing file exception.");
//        }
//        request2.setData(sendMessageDto);

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
            ByteBuffer byteBuffer = ByteBuffer.wrap(byteArrayOutputStream.toByteArray());

            ByteBuffer header = ByteBuffer.allocate(4);
            header.putInt(byteBuffer.limit());
            header.flip();
            while (header.hasRemaining()) {
                server.write(header);
            }

            while (byteBuffer.hasRemaining()) {
                server.write(byteBuffer);
            }
        } catch (IOException e) {
            logger.error("Sending message to the server failed!");
        }
    }

    private void listen() {
        ByteBuffer header = ByteBuffer.allocate(4);

        while (true) {
            header.clear();
            if (!readFromServer(header, server)) {
                return;
            }

            header.flip();
            int length = header.getInt();

            ByteBuffer data = ByteBuffer.allocate(length);
            if (!readFromServer(data, server)) {
                return;
            }

            try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data.array());
                 ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {
                ServerResponse response = (ServerResponse) objectInputStream.readObject();
                System.out.println(response.getCode());
                System.out.println(response.getMessage());
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private static boolean readFromServer(ByteBuffer data, SocketChannel channel) {
        try {
            while (data.hasRemaining()) {
                if (channel.read(data) == -1) {
                    closeChannel(channel);
                    return false;
                }
            }
        } catch (IOException e) {
            logger.error("Unable to read from server!");
            return false;
        }

        return true;
    }

    private static void closeChannel(SocketChannel channel) {
        try {
            channel.close();
            logger.info("Server channel closed!");
        } catch (IOException e) {
            logger.error("Unable to close server connection!");
        }
    }
}
