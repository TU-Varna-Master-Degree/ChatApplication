import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;

import domain.client.ServerResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DispatcherServlet {

    private static Map<Long, SocketChannel> channels = new HashMap<>();
    private static ServerSocketChannel serverSocket;
    private final static HandlerMapping handlerMapping = new HandlerMapping();
    private final static Logger logger = LogManager.getLogger();

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
                    processRequest(selector, key);
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

    private static void processRequest(Selector selector, SelectionKey key) {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer data = ByteBuffer.allocate(1024);
        int read = 0;
        try {
            read = channel.read(data);
        } catch (IOException e) {
            logger.error("Unable to read from channel!");
        }

        if (read == -1) {
            try {
                channel.close();
                logger.info("Client channel closed!");
            } catch (IOException e) {
                logger.error("Unable to close channel!");
            }
        } else {
            ServerResponse response = handlerMapping.map(selector, key, data);
            sendResponse(channel, response);
        }
    }

    private static void sendResponse(SocketChannel channel, ServerResponse response) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
            objectOutputStream.writeObject(response);
            objectOutputStream.flush();
            channel.write(ByteBuffer.wrap(byteArrayOutputStream.toByteArray()));
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
