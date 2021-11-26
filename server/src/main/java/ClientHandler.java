import java.io.IOException;
import java.util.ArrayList;

public class ClientHandler extends Thread {

    private Client client;
    private ArrayList<Client> clients;

    public ClientHandler(Client client, ArrayList<Client> clients) {
        this.client = client;
        this.clients = clients;
    }

    private void broadcast(String msg)
    {
        for (Client c : clients)
        {
            c.send( msg );
        }
    }

    private void messageUser(int idx, String msg)
    {
        clients.get( idx ).send( msg );
    }

    private void close() throws IOException {
        clients.remove( client );
        client.close();
    }

    @Override
    public void run() {
        Packet packet = client.receive();

        while(packet != null)
        {
            switch( packet.getType() )
            {
                case PEER_MSG:
                    messageUser(0, (String) packet.getContent() );
                    break;
                case SERVER_CMD:
                    break;
                case BROADCAST_MSG:
                    broadcast( (String) packet.getContent() );
                    break;
            }

            packet = client.receive();
        }

        // Done with client
        try {
            close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
