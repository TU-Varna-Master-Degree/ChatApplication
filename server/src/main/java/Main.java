import config.HibernateConfiguration;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Main {

    private static final int CHAT_PORT = 1337;

    public static void main(String[] args) {
        HibernateConfiguration.init();

        TestEchoServer server = new TestEchoServer(CHAT_PORT);
        server.run();


        HibernateConfiguration.close();
    }
}
