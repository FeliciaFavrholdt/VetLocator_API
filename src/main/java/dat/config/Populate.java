package dat.config;

import jakarta.persistence.EntityManagerFactory;
import org.jetbrains.annotations.NotNull;
import java.math.BigDecimal;
import java.util.Set;

public class Populate {
    public static void main(String[] args) {

        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

    }
}