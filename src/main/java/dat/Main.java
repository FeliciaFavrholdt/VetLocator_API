package dat;

import dat.config.ApplicationConfig;
import dat.config.HibernateConfig;
import jakarta.persistence.EntityManagerFactory;


public class Main {

    //private static final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

    public static void main(String[] args) {
        ApplicationConfig.startServer(7071);
    }
}