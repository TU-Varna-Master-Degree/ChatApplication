import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class TestEchoServer implements Runnable{
    private ServerSocket socket;
    private boolean isRunning;
    private int clientId = 0;

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
                Socket client = socket.accept();
                clientId++;

                System.out.println("Client connection started.");

                // Handle client
                int finalClientId = clientId;
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            Scanner in = new Scanner(client.getInputStream());
                            PrintWriter out = new PrintWriter(client.getOutputStream(), true);

                            String msgReceived = in.nextLine();

                            System.out.printf("Client> %s\n", msgReceived);
                            System.out.println("Server re-sent message.");
                            out.printf("Hello, Client#%d; Your Message was: %s\n", finalClientId, msgReceived);

                            client.close();
                            // TODO
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
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
