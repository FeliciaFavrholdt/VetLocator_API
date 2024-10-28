package dat.dao.impl;

import dat.config.HibernateConfig;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AppointmentDAOTest {

    private static EntityManagerFactory emf;
    private AppointmentDAO appointmentDAO;
    private EntityManager em;

    @BeforeEach
    void setUp() {
        emf = HibernateConfig.getEntityManagerFactoryForTest();
        appointmentDAO = new AppointmentDAO(emf);
    }

    @BeforeEach
    public void initEntityManager() {
        // Initialize a new EntityManager for each test
        em = emf.createEntityManager();
        em.getTransaction().begin();
    }

    @AfterEach
    public void closeEntityManager() {
        // Rollback any changes after each test to maintain test isolation
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
        em.close();
    }

    @AfterAll
    public static void tearDown() {
        if (emf != null) {
            emf.close();
        }
    }

    @Test
    void getInstance() {
        assertNotNull(appointmentDAO);
    }

    @Test
    void create() {
    }

    @Test
    void read() {
    }

    @Test
    void readAll() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }

    @Test
    void validatePrimaryKey() {
    }
}