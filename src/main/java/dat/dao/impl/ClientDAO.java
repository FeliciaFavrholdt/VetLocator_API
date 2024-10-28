package dat.dao.impl;

import dat.dao.IDAO;
import dat.dto.ClientDTO;
import dat.entities.Client;
import dat.exceptions.JpaException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class ClientDAO implements IDAO<ClientDTO, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(ClientDAO.class);  // Logger instance
    private static ClientDAO instance;
    private static EntityManagerFactory emf;

    public ClientDAO(EntityManagerFactory emf) {
        ClientDAO.emf = emf;
    }

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
            Client client = clientDTO.toEntity();
            em.persist(client);
            em.getTransaction().commit();
            logger.info("Client created successfully with ID {}", client.getId());
            return new ClientDTO(client);
        } catch (PersistenceException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            logger.error("Error creating client in the database: {}", e.getMessage());
            throw new JpaException(500, "Error creating client in the database.");
        } finally {
            em.close();
        }
    }

    @Override
    public ClientDTO read(Integer id) {
        EntityManager em = emf.createEntityManager();
        try {
            Client client = em.find(Client.class, id);
            if (client == null) {
                logger.warn("Client not found for ID: {}", id);
                throw new JpaException(404, "Client not found for ID: " + id);
            }
            logger.info("Client with ID {} successfully retrieved.", id);
            return new ClientDTO(client);
        } finally {
            em.close();
        }
    }

    @Override
    public List<ClientDTO> readAll() {
        EntityManager em = emf.createEntityManager();
        try {
            List<ClientDTO> clients = em.createQuery("SELECT new dat.dto.ClientDTO(c) FROM Client c", ClientDTO.class)
                    .getResultList();
            logger.info("Successfully retrieved {} clients.", clients.size());
            return clients;
        } finally {
            em.close();
        }
    }

    @Override
    public ClientDTO update(Integer id, ClientDTO clientDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Client client = em.find(Client.class, id);
            if (client == null) {
                logger.warn("Client not found for ID: {}", id);
                throw new JpaException(404, "Client not found for ID: " + id);
            }
            client.convertFromDTO(clientDTO);
            em.merge(client);
            em.getTransaction().commit();
            logger.info("Client with ID {} successfully updated.", id);
            return new ClientDTO(client);
        } catch (PersistenceException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            logger.error("Error updating client in the database: {}", e.getMessage());
            throw new JpaException(500, "Error updating client in the database.");
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(Integer id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Client client = em.find(Client.class, id);
            if (client == null) {
                logger.warn("Client not found for ID: {}", id);
                throw new JpaException(404, "Client not found for ID: " + id);
            }
            em.remove(client);
            em.getTransaction().commit();
            logger.info("Client with ID {} successfully deleted.", id);
        } catch (PersistenceException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            logger.error("Error deleting client from the database: {}", e.getMessage());
            throw new JpaException(500, "Error deleting client from the database.");
        } finally {
            em.close();
        }
    }

    @Override
    public boolean validatePrimaryKey(Integer id) {
        EntityManager em = emf.createEntityManager();
        try {
            boolean isValid = em.find(Client.class, id) != null;
            if (!isValid) {
                logger.warn("Invalid primary key: {}", id);
            }
            return isValid;
        } finally {
            em.close();
        }
    }
}