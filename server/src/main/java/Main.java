import config.HibernateConfiguration;
import init.DispatcherSocket;

public class Main {

    public static void main(String[] args) {
        try {
            HibernateConfiguration.init();
            DispatcherSocket.listen();
        } finally {
            DispatcherSocket.close();
            HibernateConfiguration.close();
        }
    }
}
