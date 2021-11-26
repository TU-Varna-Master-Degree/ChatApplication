import sun.misc.Queue;

import java.io.IOException;
import java.net.ServerSocket;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class TestEchoServer implements Runnable{
    private ServerSocket socket;
    private boolean isRunning;

    private ArrayList<Client> clients;

    public TestEchoServer(int port)
    {
        try{
            socket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    @Override
    public void run() {
        isRunning = true;
        try {
            do {
                // Accept client
                Client client = new Client( socket.accept() );
                clients.add( client );

                // Handle client
                System.out.println("Client connection started.");
                new ClientHandler(client, clients).start();
            }while (isRunning) ;
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        isRunning = false;
    }
}
