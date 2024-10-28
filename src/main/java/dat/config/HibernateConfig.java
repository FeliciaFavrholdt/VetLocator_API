package dat.config;

import dat.entities.*;
import dat.security.entities.Role;
import dat.security.entities.User;
import dat.util.Utils;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class HibernateConfig {

    private static final Logger logger = LoggerFactory.getLogger(HibernateConfig.class);

    private static EntityManagerFactory emf;
    private static EntityManagerFactory emfTest;
    private static Boolean isTest = false;

    public static void setTest(Boolean test) {
        isTest = test;
    }

    public static Boolean getTest() {
        return isTest;
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        if (emf == null)
            emf = createEMF(getTest());
        return emf;
    }

    public static EntityManagerFactory getEntityManagerFactoryForTest() {
        if (emfTest == null) {
            setTest(true);
            emfTest = createEMF(getTest());
        }
        return emfTest;
    }

    // Register all entities for Hibernate
    private static void getAnnotationConfiguration(Configuration configuration) {
        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(Role.class);
        configuration.addAnnotatedClass(Animal.class);
        configuration.addAnnotatedClass(Appointment.class);
        configuration.addAnnotatedClass(City.class);
        configuration.addAnnotatedClass(Client.class);
        configuration.addAnnotatedClass(Clinic.class);
        configuration.addAnnotatedClass(OpeningHours.class);
        configuration.addAnnotatedClass(Veterinarian.class);
    }

    // Create EntityManagerFactory based on the environment
    private static EntityManagerFactory createEMF(boolean forTest) {
        try {
            Configuration configuration = new Configuration();
            Properties props = new Properties();

            // Set common properties
            setBaseProperties(props);

            if (forTest) {
                props = setTestProperties(props);  // Set test-specific properties
            } else if (System.getenv("DEPLOYED") != null) {
                setDeployedProperties(props);  // Set deployed environment properties
            } else {
                props = setDevProperties(props);  // Set dev environment properties
            }

            configuration.setProperties(props);
            getAnnotationConfiguration(configuration);

            // Build session factory and unwrap EntityManagerFactory
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties())
                    .build();
            SessionFactory sf = configuration.buildSessionFactory(serviceRegistry);
            return sf.unwrap(EntityManagerFactory.class);
        } catch (Throwable ex) {
            logger.error("Initial SessionFactory creation failed.", ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    // Set common Hibernate properties
    private static Properties setBaseProperties(Properties props) {
        props.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        props.put("hibernate.connection.driver_class", "org.postgresql.Driver");
        props.put("hibernate.hbm2ddl.auto", "update");
        props.put("hibernate.current_session_context_class", "thread");
        props.put("hibernate.format_sql", "true");
        props.put("hibernate.use_sql_comments", "true");
        return props;
    }

    // Set deployed environment-specific properties
    private static Properties setDeployedProperties(Properties props) {
        String dbName = System.getenv("DB_NAME");
        props.setProperty("hibernate.connection.url", System.getenv("CONNECTION_STR") + dbName);
        props.setProperty("hibernate.connection.username", System.getenv("DB_USERNAME"));
        props.setProperty("hibernate.connection.password", System.getenv("DB_PASSWORD"));
        props.put("hibernate.show_sql", "false");
        props.put("hibernate.format_sql", "false");
        return props;
    }

    // Set dev environment-specific properties
    private static Properties setDevProperties(Properties props) {
        String dbName = Utils.getPropertyValue("DB_NAME", "config.properties");
        props.put("hibernate.connection.url", "jdbc:postgresql://localhost:5432/" + dbName);
        props.put("hibernate.connection.username", "postgres");
        props.put("hibernate.connection.password", "postgres");
        return props;
    }

    // Set test environment-specific properties with Testcontainers
    private static Properties setTestProperties(Properties props) {
        props.put("hibernate.connection.driver_class", "org.testcontainers.jdbc.ContainerDatabaseDriver");
        props.put("hibernate.connection.url", "jdbc:tc:postgresql:15.3-alpine3.18:///test_db");
        props.put("hibernate.connection.username", "postgres");
        props.put("hibernate.connection.password", "postgres");
        props.put("hibernate.hbm2ddl.auto", "create-drop");  // Reset schema for tests
        props.put("hibernate.show_sql", "true");  // Log SQL during tests
        return props;
    }
}
