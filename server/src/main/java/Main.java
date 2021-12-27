import config.HibernateConfiguration;
import init.DispatcherServlet;

public class Main {

    public static void main(String[] args) {
        try {
            HibernateConfiguration.init();
            DispatcherServlet.listen();
        } finally {
            DispatcherServlet.close();
            HibernateConfiguration.close();
        }
    }
}
