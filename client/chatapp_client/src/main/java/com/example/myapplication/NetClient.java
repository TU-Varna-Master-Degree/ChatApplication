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
        ByteBuffer header = ByteBuffer.allocate(4);

        while (true)
        {
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

    private static boolean readFromServer(ByteBuffer data, SocketChannel channel) {
        try {
            while (data.hasRemaining()) {
                if (channel.read(data) == -1) {
                    closeChannel(channel);
                    return false;
                }
            }
        } catch (IOException e) {
            System.out.println("Unable to read from server!");
            return false;
        }

        return true;
    }

    private static void closeChannel(SocketChannel channel) {
        try {
            channel.close();
            System.out.println("Server channel closed!");
        } catch (IOException e) {
            System.out.println("Unable to close server connection!");
        }
    }
}
