package dat.daos;

import dat.config.HibernateConfig;
import dat.enums.Gender;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AnimalDAOTest {

    private static EntityManagerFactory emf;
    private static EntityManager em;
    private AnimalDAO animalDao;
    private int testAnimalId;
    private int testClientId;

    @BeforeAll
    public static void setUpClass() {
        // Initialize the EntityManagerFactory for the entire test class
        emf = HibernateConfig.getEntityManagerFactoryForTest();
    }


    @BeforeEach
    public void setUp() {

        em = emf.createEntityManager();
        animalDao = new AnimalDAO(emf);

        // Begin transaction
        em.getTransaction().begin();

        // Create and persist a Client entity
        Client client = new Client();
        client.setUsername("Test Client" + System.currentTimeMillis());
        client.setEmail("testclient@example.com");
        client.setPassword("testpassword");
        client.setFirstName("Test Firstname");
        client.setLastName("Test Lastname");
        client.setGender(Gender.FEMALE);
        client.setPhone("12345678");
        em.persist(client);  // Persist the client to the test database

        // Create and persist an Animal entity associated with the Client
        Animal animal = new Animal();
        animal.setName("Test Animal");
        animal.setSpecies("Test Species");
        animal.setClient(client);  // Set the client for this animal
        animal.setAge(5);
        em.persist(animal);  // Persist the animal to the test database

        // Commit transaction
        em.getTransaction().commit();

        testAnimalId = animal.getId();  // Store the generated ID for testing
        testClientId = client.getId();  // Store the generated ID for testing
    }



    @Test
    void getAll() {
        // Testing readAll method from AnimalDAO
        List<AnimalDTO> animals = animalDao.readAll();  // Get all animals using DAO

        // Verify that the animals list is not empty and has at least 1 animal
        assertNotNull(animals);
        assertFalse(animals.isEmpty());

        // Check the properties of the first animal
        AnimalDTO firstAnimal = animals.get(0);
        assertEquals("Test Animal", firstAnimal.getName());
        assertEquals("Test Species", firstAnimal.getSpecies());
    }

    @Test
    void createAnimal() {
        // Prepare an AnimalDTO
        AnimalDTO animalDTO = new AnimalDTO();
        animalDTO.setName("New Animal");
        animalDTO.setSpecies("New Species");
        animalDTO.setAge(3);
        animalDTO.setUserId(1);  // Assuming the Client's ID is 1 after persisting

        // Use the create method in AnimalDAO
        AnimalDTO createdAnimal = animalDao.create(animalDTO);

        // Verify that the createdAnimal is not null
        assertNotNull(createdAnimal);

        // Verify that the properties were correctly set
        assertEquals("New Animal", createdAnimal.getName());
        assertEquals("New Species", createdAnimal.getSpecies());
        assertEquals(3, createdAnimal.getAge());

        // Check that the client was correctly set in the animal entity
        assertEquals(1, createdAnimal.getUserId());  // Ensure the relationship is maintained
    }


    @Test
    void readAnimalById() {
        // Use the read method in AnimalDAO to retrieve the AnimalDTO by its ID
        AnimalDTO animalDTO = animalDao.read(testAnimalId);

        // Verify that the AnimalDTO is not null
        assertNotNull(animalDTO);

        // Verify that the properties match the expected values
        assertEquals("Test Animal", animalDTO.getName());
        assertEquals("Test Species", animalDTO.getSpecies());
        assertEquals(5, animalDTO.getAge());
        assertEquals(1, animalDTO.getUserId()); // Assuming the Client's ID is 1
    }

    @Test
    void readAnimalByInvalidId() {
        // Try reading an AnimalDTO with an invalid/nonexistent ID
        AnimalDTO animalDTO = animalDao.read(9999);  // Nonexistent ID

        // Verify that the returned value is null
        assertNull(animalDTO);
    }

    @Test
    void updateAnimal() {
        // Create a new Client to change ownership of the animal
        em.getTransaction().begin();
        Client newClient = new Client();
        newClient.setUsername("New Owner");
        newClient.setFirstName("New firstname");
        newClient.setLastName("New lastname");
        newClient.setEmail("newowner@example.com");
        newClient.setPassword("newpassword");
        newClient.setFirstName("New");
        newClient.setGender(Gender.MALE);
        newClient.setPhone("12345678");
        em.persist(newClient);  // Persist the new client
        em.getTransaction().commit();
        int newClientId = newClient.getId();

        // Prepare updated AnimalDTO
        AnimalDTO updatedAnimalDTO = new AnimalDTO();
        updatedAnimalDTO.setName("Updated Animal");
        updatedAnimalDTO.setSpecies("Updated Species");
        updatedAnimalDTO.setAge(10);
        updatedAnimalDTO.setUserId(newClientId);  // Change to the new client ID

        // Use the update method in AnimalDAO
        AnimalDTO updatedAnimal = animalDao.update(testAnimalId, updatedAnimalDTO);

        // Verify that the updatedAnimal is not null
        assertNotNull(updatedAnimal);

        // Verify that the properties were correctly updated
        assertEquals("Updated Animal", updatedAnimal.getName());
        assertEquals("Updated Species", updatedAnimal.getSpecies());
        assertEquals(10, updatedAnimal.getAge());
        assertEquals(newClientId, updatedAnimal.getUserId());
    }

    @Test
    void updateAnimalNonExistent() {
        // Prepare an AnimalDTO for a non-existent animal
        AnimalDTO updatedAnimalDTO = new AnimalDTO();
        updatedAnimalDTO.setName("Non-Existent Animal");
        updatedAnimalDTO.setSpecies("Non-Existent Species");
        updatedAnimalDTO.setAge(3);
        updatedAnimalDTO.setUserId(testClientId);

        // Try to update an animal with an invalid ID (e.g., 9999)
        AnimalDTO result = animalDao.update(9999, updatedAnimalDTO);

        // Verify that the result is null since the animal does not exist
        assertNull(result);
    }

    @Test
    void deleteAnimalById() {
        // Delete the animal using the delete method
        animalDao.delete(testAnimalId);

        // Verify that the animal no longer exists in the database
        EntityManager em = emf.createEntityManager();
        Animal deletedAnimal = em.find(Animal.class, testAnimalId);
        assertNull(deletedAnimal);  // The animal should be null since it was deleted
        em.close();
    }

    @Test
    void deleteAnimalNonExistentId() {
        // Try to delete an animal with a non-existent ID (e.g., 9999)
        animalDao.delete(9999);

        // Since there's no such animal, the test should just confirm that no exceptions were thrown
        EntityManager em = emf.createEntityManager();
        Animal nonExistentAnimal = em.find(Animal.class, 9999);
        assertNull(nonExistentAnimal);  // Still null, since no animal with that ID exists
        em.close();
    }


    @AfterEach
    public void tearDown() {
        if (em != null) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();  // Rollback to undo changes
            }
            em.close();
        }
    }
}