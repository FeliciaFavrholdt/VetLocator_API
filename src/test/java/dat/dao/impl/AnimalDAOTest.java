package dat.dao.impl;

import dat.config.HibernateConfig;
import dat.dao.impl.AnimalDAO;
import dat.dto.AnimalDTO;
import dat.entities.Animal;
import dat.entities.Client;
import dat.exceptions.JpaException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AnimalDAOTest {

    private static EntityManagerFactory emf;
    private AnimalDAO animalDAO;
    private EntityManager em;

    @BeforeAll
    public void setUp() {
        // Initialize the EntityManagerFactory for the test environment
        emf = HibernateConfig.getEntityManagerFactoryForTest();
        animalDAO = new AnimalDAO(emf);
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
    public void tearDown() {
        if (emf != null) {
            emf.close();
        }
    }

    @Test
    void getInstance() {
        // Test if the AnimalDAO instance is not null
        assertNotNull(animalDAO);
    }

    @Test
    void create() {
        // Create a new AnimalDTO object
        AnimalDTO animalDTO = new AnimalDTO();
        animalDTO.setName("Test Animal");
        animalDTO.setOwnerId(1L);

        // Create a new Animal object
        Animal animal = animalDTO.toEntity();
        Client client = em.find(Client.class, animalDTO.getOwnerId());

        // Check if the client exists
        if (client == null) {
            // Log a warning message
            System.out.println("Client not found for userId: " + animalDTO.getOwnerId());
            // Throw a JpaException
            throw new JpaException(400, "Client not found for userId: " + animalDTO.getOwnerId());
        }

        // Set the owner of the animal
        animal.setOwner(client);

        // Persist the animal object
        em.persist(animal);

        // Commit the transaction
        em.getTransaction().commit();

        // Log an info message
        System.out.println("Animal created successfully with ID " + animal.getId());

        // Create a new AnimalDTO object from the animal object
        AnimalDTO newAnimalDTO = new AnimalDTO(animal);

        // Test if the newAnimalDTO object is not null
        assertNotNull(newAnimalDTO);
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
