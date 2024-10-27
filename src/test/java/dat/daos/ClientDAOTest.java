//package dat.daos;
//
//import dat.config.HibernateConfig;
//import dat.dao.impl.ClientDAO;
//import dat.dto.ClientCreateDTO;
//import dat.dto.ClientDTO;
//import dat.entities.Client;
//import dat.enums.Gender;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.EntityManagerFactory;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import static org.junit.jupiter.api.Assertions.*;
//import java.util.List;
//
//public class ClientDAOTest {
//
//    private EntityManagerFactory emf;
//    private EntityManager em;
//    private ClientDAO userDao;
//    private int testClientId;
//
//    @BeforeEach
//    public void setUp() {
//        // Initialize the EntityManagerFactory for test purposes
//        emf = HibernateConfig.getEntityManagerFactoryForTest();
//        em = emf.createEntityManager();
//        userDao = ClientDAO.getInstance(emf);
//
//        // Begin transaction
//        em.getTransaction().begin();
//
//        // Create and persist a Client entity
//        Client client = new Client();
//        client.setUsername("TestUser");
//        client.setEmail("testuser@example.com");
//        client.setPassword("password123");
//        client.setFirstName("Test");
//        client.setLastName("dat.security.entities.User");
//        client.setGender(Gender.FEMALE);
//        client.setPhone("1234567890");
//        em.persist(client);  // Persist the client to the test database
//        em.getTransaction().commit();
//
//        testClientId = client.getId();  // Store the generated client ID for testing
//    }
//
//    @Test
//    void createClient() {
//        // Prepare ClientCreateDTO for creation (includes password)
//        ClientCreateDTO client = new ClientCreateDTO();
//        client.setFirstName("New");
//        client.setLastName("dat.security.entities.User");
//        client.setPhone("0987654321");
//        client.setGender(Gender.MALE);
//        client.setEmail("newuser@example.com");
//        client.setPassword("password123");
//        client.setUsername("NewUser");
//
//        // Use the create method to add a new client
//        ClientDTO createdClient = userDao.create(client);  // Should return ClientDTO, not ClientCreateDTO
//
//        // Verify that the created client is not null
//        assertNotNull(createdClient);
//
//        // Verify the properties of the newly created client
//        assertEquals("New dat.security.entities.User", createdClient.getFullName());
//        assertEquals("newuser@example.com", createdClient.getEmail());
//        assertEquals("0987654321", createdClient.getPhone());
//    }
//
//
//    @Test
//    void readClientById() {
//        // Use the read method to get the client by its ID
//        ClientDTO clientDTO = userDao.read(testClientId);
//
//        // Verify that the client was found
//        assertNotNull(clientDTO);
//
//        // Verify the client's properties
//        assertEquals("Test dat.security.entities.User", clientDTO.getFullName());
//        assertEquals("testuser@example.com", clientDTO.getEmail());
//        assertEquals("1234567890", clientDTO.getPhone());
//    }
//
//    @Test
//    void readAllClients() {
//        // Use the readAll method to retrieve all clients
//        List<ClientDTO> clients = userDao.readAll();
//
//        // Verify that the list is not empty and contains at least one client
//        assertNotNull(clients);
//        assertFalse(clients.isEmpty());
//
//        // Verify the properties of the first client in the list
//        ClientDTO firstClient = clients.get(0);
//        assertEquals("Test dat.security.entities.User", firstClient.getFullName());
//    }
//
//    @Test
//    void updateClient() {
//        // Prepare updated ClientDTO
//        ClientDTO updatedClientDTO = new ClientDTO();
//        updatedClientDTO.setFullName("Updated dat.security.entities.User");
//        updatedClientDTO.setEmail("updateduser@example.com");
//        updatedClientDTO.setPhone("1122334455");
//
//        // Use the update method to update the client
//        ClientDTO updatedClient = userDao.update(testClientId, updatedClientDTO);
//
//        // Verify that the updated client is not null
//        assertNotNull(updatedClient);
//
//        // Verify the updated properties of the client
//        assertEquals("Updated dat.security.entities.User", updatedClient.getFullName());
//        assertEquals("updateduser@example.com", updatedClient.getEmail());
//        assertEquals("1122334455", updatedClient.getPhone());
//    }
//
//    @Test
//    void deleteClientById() {
//        // Delete the client using the delete method
//        userDao.delete(testClientId);
//
//        // Verify that the client no longer exists in the database
//        ClientDTO deletedClient = userDao.read(testClientId);
//        assertNull(deletedClient);  // The client should be null since it was deleted
//    }
//
//    @Test
//    void validatePrimaryKey() {
//        // Validate that the primary key exists
//        boolean isValid = userDao.validatePrimaryKey(testClientId);
//        assertTrue(isValid);
//
//        // Validate that a non-existent primary key returns false
//        boolean isInvalid = userDao.validatePrimaryKey(9999);
//        assertFalse(isInvalid);
//    }
//
//    @AfterEach
//    public void tearDown() {
//        if (em != null) {
//            em.close();  // Close the EntityManager
//        }
//        if (emf != null) {
//            emf.close();  // Close the EntityManagerFactory
//        }
//    }
//}
