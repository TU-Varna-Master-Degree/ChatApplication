package com.example.myapplication;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import domain.client.dialogue.ServerRequest;
import domain.client.dialogue.ServerResponse;
import domain.client.dto.UserDto;
import domain.client.enums.OperationType;

public class NetClient implements Serializable
{
    private static final ExecutorService executorService = Executors.newFixedThreadPool(3);
    private static SocketChannel server;
    private static final ArrayList<Consumer<ServerResponse>> handlerList = new ArrayList<>();
    
    public static void register(Consumer<ServerResponse> handler)
    {
        handlerList.add(handler);
    }
    
    public static void unregister(Consumer<ServerResponse> handler)
    {
        handlerList.remove(handler);
    }
    
    public boolean isRunning()
    {
        return server.isOpen();
    }
    
    public static void start(String ip, int port) throws IOException
    {
        server = SocketChannel.open( new InetSocketAddress(ip, port) );
        
        executorService.execute( NetClient::listen );
    }
    
    public static void stop() throws IOException
    {
        executorService.shutdown();
        server.close();
    }
    
    public static void sendRequest(ServerRequest serverRequest)
    {
        executorService.execute(() ->
        {
            send(serverRequest);
        });
    }
    
    private static void send(ServerRequest serverRequest)
    {
        System.out.println( "[SEND]" );
        
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream))
        {
            objectOutputStream.writeObject(serverRequest);
            objectOutputStream.flush();
            System.out.println( "[WRITE OBJ]" );
            server.write(ByteBuffer.wrap(byteArrayOutputStream.toByteArray()));
        }
        catch (IOException e)
        {
            int a = 6;
            System.out.println( e.getMessage() );
            // logger.error("Sending message to the server failed!");
        }
    }
    
    private static void listen()
    {
        while (true)
        {
            // TODO : Realloc ???
            ByteBuffer data = ByteBuffer.allocate(1024);
            try
            {
                server.read(data);
            }
            catch (IOException e)
            {
               // logger.error("Unable to read data from server!");
            }

            try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data.array());
                 ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream))
            {
                ServerResponse response = (ServerResponse) objectInputStream.readObject();
                
                System.out.println(response.getCode());
                System.out.println(response.getMessage());
                
                // Dispatch
                for(Consumer<ServerResponse> handler : handlerList)
                {
                    executorService.execute( ()-> handler.accept(response) );
                }
            }
            catch (IOException | ClassNotFoundException e)
            {
                e.printStackTrace();
            }
            data.clear();
        }
    }
    
    public static void main(String[] args) throws IOException
    {
        NetClient.start("95.42.42.125", 1300);
        
        // Register request
//        ServerRequest<User> request = new ServerRequest<>(OperationType.USER_REGISTER);
//        User user = new User();
//        user.setUsername("Gosho");
//        user.setEmail("gosho44@abv.bg");
//        user.setPassword("1111");
//        request.setData(user);
        
        // Login request
    
        ServerRequest<UserDto> request =  new ServerRequest<>(OperationType.USER_LOGIN);
        UserDto user = new UserDto();
        user.setEmail("gosho44@abv.bg");
        user.setPassword("11111");
        request.setData(user);
    
        NetClient.sendRequest(request);
    }
}
