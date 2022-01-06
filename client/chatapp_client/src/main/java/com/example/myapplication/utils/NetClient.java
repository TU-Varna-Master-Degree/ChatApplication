package com.example.myapplication.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import domain.client.dialogue.ServerRequest;
import domain.client.dialogue.ServerResponse;

public class NetClient
{
    private final ArrayList<Consumer<ServerResponse>> handlerList = new ArrayList<>();
    
    private final String ipAddress;
    private final int serverPort;
    private final ExecutorService executorService = Executors.newFixedThreadPool(2);
    private static SocketChannel server;
    
    public NetClient(String ipAddress, int serverPort)
    {
        this.ipAddress = ipAddress;
        this.serverPort = serverPort;
    }
    
    public void start() {
        executorService.execute(() -> {
            try {
                server = SocketChannel.open(new InetSocketAddress(ipAddress, serverPort));
                NetClient.this.listen();
            } catch (IOException e) {
                System.out.println("Failed to connect to server");
            }
        });
    }
    
    public boolean isRunning()
    {
        return server.isOpen();
    }
    
    public void stop() throws IOException {
        executorService.shutdown();
        server.close();
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
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void listen() {
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
            catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            data.clear();
        }
    }

    private boolean readFromServer(ByteBuffer data, SocketChannel channel) {
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
    
    public void register(Consumer<ServerResponse> handler) {
        handlerList.add(handler);
    }
    
    public void unregister(Consumer<ServerResponse> handler) {
        handlerList.remove(handler);
    }
    
    private void closeChannel(SocketChannel channel) {
        try {
            channel.close();
            System.out.println("Server channel closed!");
        } catch (IOException e) {
            System.out.println("Unable to close server connection!");
        }
    }
}