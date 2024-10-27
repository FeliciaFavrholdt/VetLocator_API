package dat.dao.impl;

import dat.dao.IDAO;
import dat.dto.ClientCreateDTO;
import dat.dto.ClientDTO;
import dat.entities.Client;
import dat.exception.JpaException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.PersistenceException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class ClientDAO implements IDAO<ClientDTO, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(ClientDAO.class);

    public static ClientDAO instance;
    public static EntityManagerFactory emf;

    public static ClientDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new ClientDAO();
        }
        return instance;
    }

    @Override
    public ClientDTO create(ClientDTO clientDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            // Convert DTO to Client entity
            Client client = new Client();
            client.setFirstName(clientDTO.getFullName());
            client.setEmail(clientDTO.getEmail());
            client.setPhone(clientDTO.getPhone());
            client.setGender(clientDTO.getGender());

            em.persist(client);
            em.getTransaction().commit();
            logger.info("Client created successfully with ID: {}", client.getId());
            return new ClientDTO(client);
        } catch (PersistenceException e) {
            em.getTransaction().rollback();
            logger.error("Error creating client: {}", e.getMessage(), e);
            throw new JpaException(500, "Error creating client in the database.");
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }


//    @Override
//    public ClientDTO create(ClientDTO clientDTO) {
//        EntityManager em = emf.createEntityManager();
//        try {
//            em.getTransaction().begin();
//
//            // Convert DTO to Client entity
//            Client client = new Client();
//            client.setFirstName(clientDTO.getFirstName());
//            client.setLastName(clientDTO.getLastName());
//            client.setEmail(clientDTO.getEmail());
//            client.setPhone(clientDTO.getPhone());
//            client.setGender(clientDTO.getGender());
//            client.setUsername(clientDTO.getUsername());
//            client.setPassword(hashPassword(clientDTO.getPassword()));  // Hash the password before storing
//
//            em.persist(client);
//            em.getTransaction().commit();
//
//            logger.info("Client created successfully with ID: {}", client.getId());
//            return new ClientDTO(client);  // Return standard ClientDTO without sensitive fields
//        } catch (PersistenceException e) {
//            em.getTransaction().rollback();
//            logger.error("Error creating client: {}", e.getMessage(), e);
//            throw new JpaException(500, "Error creating client in the database.");
//        } finally {
//            if (em.isOpen()) {
//                em.close();
//            }
//        }
//    }

    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    @Override
    public ClientDTO read(Integer id) {
        EntityManager em = emf.createEntityManager();
        try {
            Client client = em.find(Client.class, id);
            if (client == null) {
                throw new JpaException(404, "Client not found for ID: " + id);
            }
            return new ClientDTO(client);
        } catch (PersistenceException e) {
            logger.error("Error reading client with ID {}: {}", id, e.getMessage(), e);
            throw new JpaException(500, "Error reading client from the database.");
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public List<ClientDTO> readAll() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<ClientDTO> query = em.createQuery("SELECT new dat.dto.ClientDTO(u) FROM Client u", ClientDTO.class);
            List<ClientDTO> clients = query.getResultList();
            logger.info("Fetched {} clients from the database.", clients.size());
            return clients;
        } catch (PersistenceException e) {
            logger.error("Error fetching all clients: {}", e.getMessage(), e);
            throw new JpaException(500, "Error fetching clients from the database.");
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }


    @Override
    public ClientDTO update(Integer id, ClientDTO clientDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Client client = em.find(Client.class, id);
            if (client == null) {
                throw new JpaException(404, "Client not found for ID: " + id);
            }

            // Update the entity with the new DTO data
            client.updateFromDTO(clientDTO);
            Client mergedClient = em.merge(client);
            em.getTransaction().commit();
            logger.info("Client updated successfully with ID: {}", mergedClient.getId());
            return new ClientDTO(mergedClient);
        } catch (PersistenceException e) {
            em.getTransaction().rollback();
            logger.error("Error updating client with ID {}: {}", id, e.getMessage(), e);
            throw new JpaException(500, "Error updating client in the database.");
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public void delete(Integer id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Client client = em.find(Client.class, id);
            if (client == null) {
                throw new JpaException(404, "Client not found for ID: " + id);
            }
            em.remove(client);
            em.getTransaction().commit();
            logger.info("Client deleted successfully with ID: {}", id);
        } catch (PersistenceException e) {
            em.getTransaction().rollback();
            logger.error("Error deleting client with ID {}: {}", id, e.getMessage(), e);
            throw new JpaException(500, "Error deleting client from the database.");
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public boolean validatePrimaryKey(Integer id) {
        EntityManager em = emf.createEntityManager();
        try {
            Client client = em.find(Client.class, id);
            return client != null;
        } catch (PersistenceException e) {
            logger.error("Error validating client primary key with ID {}: {}", id, e.getMessage(), e);
            throw new JpaException(500, "Error validating client primary key.");
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }
}