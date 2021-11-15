import config.HibernateConfiguration;

public class Main {
    public static void main(String[] args) {
        HibernateConfiguration.init();
        HibernateConfiguration.close();
    }
}
