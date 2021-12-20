import domain.client.ServerRequest;
import domain.client.ServerResponse;
import domain.entities.User;
import domain.enums.OperationType;
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

        // Register request
//        ServerRequest<User> request = new ServerRequest<>(OperationType.USER_REGISTER);
//        User user = new User();
//        user.setUsername("Gosho");
//        user.setEmail("gosho44@abv.bg");
//        user.setPassword("1111");
//        request.setData(user);

        // Login request
        ServerRequest<User> request = new ServerRequest<>(OperationType.USER_LOGIN);
        User user = new User();
        user.setEmail("gosho44@abv.bg");
        user.setPassword("11111");
        request.setData(user);

        serverHandler.sendRequest(request);
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
            ByteBuffer data = ByteBuffer.allocate(1024);
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
            serverHandler.stop();
            break;
        }
    }
}
