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

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class ClientDAO implements IDAO<ClientDTO, Integer> {

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
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Client client = clientDTO.toEntity();
            em.persist(client);
            em.getTransaction().commit();
            return new ClientDTO(client);
        } catch (PersistenceException e) {
            throw new JpaException(500, "Error creating client in the database.");
        }
    }

    @Override
    public ClientDTO read(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            Client client = em.find(Client.class, id);
            if (client == null) {
                throw new JpaException(404, "Client not found for ID: " + id);
            }
            return new ClientDTO(client);
        }
    }

    @Override
    public List<ClientDTO> readAll() {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery("SELECT new dat.dto.ClientDTO(c) FROM Client c", ClientDTO.class)
                    .getResultList();
        }
    }

    @Override
    public ClientDTO update(Integer id, ClientDTO clientDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Client client = em.find(Client.class, id);
            if (client == null) {
                throw new JpaException(404, "Client not found for ID: " + id);
            }
            client.updateFromDTO(clientDTO);
            em.merge(client);
            em.getTransaction().commit();
            return new ClientDTO(client);
        } catch (PersistenceException e) {
            throw new JpaException(500, "Error updating client in the database.");
        }
    }

    @Override
    public void delete(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Client client = em.find(Client.class, id);
            if (client == null) {
                throw new JpaException(404, "Client not found for ID: " + id);
            }
            em.remove(client);
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            throw new JpaException(500, "Error deleting client from the database.");
        }
    }

    @Override
    public boolean validatePrimaryKey(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(Client.class, id) != null;
        }
    }
}

