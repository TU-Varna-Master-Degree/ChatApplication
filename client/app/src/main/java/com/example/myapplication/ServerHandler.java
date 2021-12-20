package com.example.myapplication;

import com.example.myapplication.models.User;
import com.example.myapplication.models.client.ServerRequest;
import com.example.myapplication.models.client.ServerResponse;
import com.example.myapplication.models.enums.OperationType;

// import org.apache.logging.log4j.LogManager;
// import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ServerHandler
{

    // private final static Logger logger = LogManager.getLogger();
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
    
    static public void start() {
        try {
            server = SocketChannel.open(new InetSocketAddress("95.42.42.125", 1300));
        } catch (IOException e) {
            // logger.error("Unable to connect with server!");
            System.exit(1);
        }
        executorService = Executors.newFixedThreadPool(2);
        executorService.execute(ServerHandler::listen);
        
        String obj = "";
    }
    
    static public void stop() {
        executorService.shutdown();
        try {
            server.close();
        } catch (IOException e) {
            // logger.error("Unable to close connection with server!");
        }
    }
    
    static public void sendRequest(ServerRequest serverRequest) {
        executorService.execute(() -> send(serverRequest));
    }
    
    static private void send(ServerRequest serverRequest) {
        System.out.println( "[SEND]" );
       
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
            objectOutputStream.writeObject(serverRequest);
            objectOutputStream.flush();
            System.out.println( "[WRITE OBJ]" );
            server.write(ByteBuffer.wrap(byteArrayOutputStream.toByteArray()));
        } catch (IOException e) {
            int a = 6;
            System.out.println( e.getMessage() );
            // logger.error("Sending message to the server failed!");
        }
    }
    
    static private void listen() {
        while (true) {
            ByteBuffer data = ByteBuffer.allocate(1024);
            try {
                server.read(data);
            } catch (IOException e) {
               // logger.error("Unable to read data from server!");
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
        }
    }
}
