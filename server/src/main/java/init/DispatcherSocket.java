package init;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import domain.client.dialogue.ServerResponse;
import domain.enums.StatusCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.SessionService;
import service.impl.SessionServiceImpl;

public class DispatcherSocket {

    private final static Logger logger = LogManager.getLogger();
    private final static ObjectMapper objectMapper;
    private static ServerSocketChannel serverSocket;
    private static HandlerMapping handlerMapping;
    private static SessionService sessionService;

    static {
        objectMapper = JsonMapper.builder()
            .findAndAddModules()
            .build();
    }

    public static void listen() {
        try {
            serverSocket = ServerSocketChannel.open();
            serverSocket.bind(new InetSocketAddress(1300));
            serverSocket.configureBlocking(false);
        } catch (IOException e) {
            logger.error("Unable to open socket channel!");
            System.exit(1);
        }

        Selector selector = null;
        sessionService = new SessionServiceImpl();
        handlerMapping = new HandlerMapping(sessionService, objectMapper);

        try {
            selector = Selector.open();
            serverSocket.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            logger.error("Unable to register accept channel!");
            System.exit(1);
        }

        while (true) {
            try {
                selector.select();
            } catch (IOException e) {
                logger.error("Selecting process was interrupted!");
            }
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iter = selectionKeys.iterator();

            while (iter.hasNext()) {
                SelectionKey key = iter.next();

                if (key.isAcceptable()) {
                    registerChannel(selector, serverSocket);
                }

                if (key.isReadable()) {
                    processRequest(key);
                }

                iter.remove();
            }
        }
    }

    public static void close() {
        try {
            serverSocket.close();
            logger.info("Server socket closed!");
        } catch (IOException e) {
            logger.info("Closing server socket failed!");
        }
    }

    private static void processRequest(SelectionKey key) {
        SocketChannel channel = (SocketChannel) key.channel();

        ByteBuffer header = ByteBuffer.allocate(4);
        if (!readFromChannel(header, key)) {
            return;
        }

        header.flip();
        int length = header.getInt();

        ByteBuffer data = ByteBuffer.allocate(length);
        if (!readFromChannel(data, key)) {
            return;
        }

        ServerResponse response = handlerMapping.map(key, data);
        if (!response.getCode().equals(StatusCode.EMPTY)) {
            sendResponse(channel, response);
        }
    }

    private static boolean readFromChannel(ByteBuffer data, SelectionKey key) {
        SocketChannel channel = (SocketChannel) key.channel();

        try {
            while (data.hasRemaining()) {
                if (channel.read(data) == -1) {
                    closeChannel(key);
                    return false;
                }
            }
        } catch (IOException e) {
            closeChannel(key);
            logger.error("Unable to read from channel!");
            return false;
        }

        return true;
    }

    private static void closeChannel(SelectionKey key) {
        try {
            sessionService.destroySession(key);
            key.channel().close();
            logger.info("Client channel closed!");
        } catch (IOException e) {
            logger.error("Unable to close client!");
        }
    }

    public static void sendResponse(SocketChannel channel, ServerResponse response) {
        try {
            byte[] responseBytes = objectMapper.writeValueAsBytes(response);
            ByteBuffer byteBuffer = ByteBuffer.wrap(responseBytes);

            ByteBuffer header = ByteBuffer.allocate(4);
            header.putInt(byteBuffer.limit());
            header.flip();
            while (header.hasRemaining()) {
                channel.write(header);
            }

            while (byteBuffer.hasRemaining()) {
                channel.write(byteBuffer);
            }
        } catch (JsonProcessingException ex) {
            logger.error("Parsing response to client failed!");
        } catch (IOException ex) {
            logger.error("Sending response to client failed!");
        }
    }

    private static void registerChannel(Selector selector, ServerSocketChannel serverSocket) {
        try {
            SocketChannel client = serverSocket.accept();
            client.configureBlocking(false);
            client.register(selector, SelectionKey.OP_READ);
            logger.info("Connection on - " + client.socket());
        } catch (IOException e) {
            logger.error("Unable to register client channel!");
            e.printStackTrace();
        }
    }
}
