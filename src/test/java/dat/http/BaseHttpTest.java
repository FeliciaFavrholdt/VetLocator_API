package dat.integration;

import dat.config.ApplicationConfig;
import dat.config.HibernateConfig;
import io.javalin.Javalin;
import io.restassured.RestAssured;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.containers.PostgreSQLContainer;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BaseHttpTest {

    private EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryForTest();
    private Javalin app;

    // Initialize Testcontainers PostgreSQL
    public static PostgreSQLContainer<?> postgresqlContainer = new PostgreSQLContainer<>("postgres:15.3-alpine")
            .withDatabaseName("test_db")
            .withUsername("postgres")
            .withPassword("postgres");

    @BeforeAll
    public void setUp() {
        postgresqlContainer.start();

        // Set Hibernate to test mode and use Testcontainers DB
        HibernateConfig.setTest(true);

        // Start the Javalin server with the test EntityManagerFactory
        app = ApplicationConfig.startServer(7000, emf);

        // Set RestAssured base URI for HTTP requests
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 7000;
    }

    @AfterAll
    public void tearDown() {
        // Stop the Javalin server
        ApplicationConfig.stopServer(app);

        // Stop the Testcontainers database
        postgresqlContainer.stop();
    }
}