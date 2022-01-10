package com.example.myapplication.utils;

import com.example.myapplication.domain.dialogue.ServerRequest;
import com.example.myapplication.domain.dialogue.ServerResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class NetClient {

    private final String ipAddress;
    private final int serverPort;

    private final ExecutorService executorService = Executors.newFixedThreadPool(2);
    private final ObjectMapper objectMapper;
    private SocketChannel server;
    private final ArrayList<Consumer<ServerResponse>> handlerList = new ArrayList<>();

    public NetClient(String ipAddress, int serverPort)
    {
        this.ipAddress = ipAddress;
        this.serverPort = serverPort;
        this.objectMapper = JsonMapper.builder()
                .findAndAddModules()
                .build();
    }

    public void register(Consumer<ServerResponse> handler) {
        handlerList.add(handler);
    }
    
    public void unregister(Consumer<ServerResponse> handler) {
        handlerList.remove(handler);
    }
    
    public void start() {
        executorService.execute(() -> {
            try {
                server = SocketChannel.open(new InetSocketAddress(ipAddress, serverPort));
                listen();
            } catch (IOException e) {
                System.out.println("Failed to connect to server");
            }
        });
    }
    
    public void stop() throws IOException {
        executorService.shutdown();
        server.close();
    }

    public void executeTask(Runnable runnable) {
        executorService.execute(runnable);
    }
    
    public void sendRequest(ServerRequest serverRequest) {
        executorService.execute(() -> send(serverRequest));
    }

    private void send(ServerRequest serverRequest) {
        try {
            byte[] responseBytes = objectMapper.writeValueAsBytes(serverRequest);
            ByteBuffer byteBuffer = ByteBuffer.wrap(responseBytes);

            ByteBuffer header = ByteBuffer.allocate(4);
            header.putInt(byteBuffer.limit());
            header.flip();
            while (header.hasRemaining()) {
                server.write(header);
            }

            while (byteBuffer.hasRemaining()) {
                server.write(byteBuffer);
            }
        } catch (JsonProcessingException ex) {
            System.out.println("Parsing message to the server failed!");
        } catch (IOException e) {
            System.out.println("Sending message to the server failed!");
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

            try {
                ServerResponse response = objectMapper.readValue(data.array(), ServerResponse.class);

                // Dispatch
                for (Consumer<ServerResponse> handler : handlerList) {
                    executorService.execute(() -> handler.accept(response));
                }
            } catch (IOException e) {
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

    public <T> T getData(ServerResponse responseDate, Class<T> tClass) {
        return objectMapper.convertValue(responseDate.getData(), tClass);
    }

    public <T> List<T> getDataList(ServerResponse responseDate, Class<T> tClass) {
        return objectMapper.convertValue(responseDate.getData(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, tClass));
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