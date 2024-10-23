package dat.dao.impl;

import dat.dao.IDAO;
import dat.dto.ClientDTO;
import dat.entities.Client;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class ClientDAO implements IDAO<ClientDTO, Integer> {

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
            // Convert DTO to User entity
            Client client = new Client(clientDTO);
            em.persist(client);
            em.getTransaction().commit();
            // Return the persisted entity as a DTO
            return new ClientDTO(client);
        } finally {
            em.close();
        }
    }

    @Override
    public ClientDTO read(Integer id) {
        EntityManager em = emf.createEntityManager();
        try {
            Client client = em.find(Client.class, id);
            return client != null ? new ClientDTO(client) : null;
        } finally {
            em.close();
        }
    }

    @Override
    public List<ClientDTO> readAll() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<ClientDTO> query = em.createQuery("SELECT new dat.dto.ClientDTO(u) FROM Client u", ClientDTO.class);
            return query.getResultList();
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
            if (client != null) {
                // Update the entity with the new DTO data
                client.updateFromDTO(clientDTO);
                Client mergedClient = em.merge(client);
                em.getTransaction().commit();
                return new ClientDTO(mergedClient);
            }
            return null;
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
            if (client != null) {
                em.remove(client);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Override
    public boolean validatePrimaryKey(Integer id) {
        EntityManager em = emf.createEntityManager();
        try {
            Client client = em.find(Client.class, id);
            return client != null;
        } finally {
            em.close();
        }
    }
}
