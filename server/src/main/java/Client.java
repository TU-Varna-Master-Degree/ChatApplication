import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private PrintWriter writer;
    private Scanner scanner;
    private Socket socket;

    public Client(Socket socket) throws IOException
    {
        this.socket = socket;
        this.scanner = new Scanner(socket.getInputStream());
        this.writer = new PrintWriter(socket.getOutputStream(), true);
    }

    public Packet receive()
    {
        Packet packet = new Packet();
        switch(scanner.next())
        {
            case "SERVER_CMD":
                packet.setType(Packet.Header.SERVER_CMD);
                break;
            case "PEER_MSG":
                packet.setType(Packet.Header.PEER_MSG);
                break;
            case "BROADCAST_MSG":
                packet.setType(Packet.Header.BROADCAST_MSG);
                break;
            default:
                return null;
        }

        packet.setContent( scanner.nextLine() );

        return packet;
    }

    public void send(String msg)
    {
        writer.write(msg);
    }

    public void close() throws IOException {
        socket.close();
    }

}
