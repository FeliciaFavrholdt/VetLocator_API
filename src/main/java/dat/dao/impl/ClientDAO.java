package dat.dao.impl;

import dat.dao.IDAO;
import dat.dto.ClientCreateDTO;
import dat.dto.ClientDTO;
import dat.entities.Client;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;

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

    public ClientDTO create(ClientCreateDTO clientDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            // Convert DTO to Client entity
            Client client = new Client();
            client.setFirstName(clientDTO.getFirstName());
            client.setLastName(clientDTO.getLastName());
            client.setEmail(clientDTO.getEmail());
            client.setPhone(clientDTO.getPhone());
            client.setGender(clientDTO.getGender());
            client.setUsername(clientDTO.getUsername());
            client.setPassword(hashPassword(clientDTO.getPassword()));  // Hash the password before storing

            em.persist(client);
            em.getTransaction().commit();

            return new ClientDTO(client);  // Return standard ClientDTO without sensitive fields
        } finally {
            em.close();
        }
    }

    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
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
    public ClientDTO create(ClientDTO clientDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            // Handle creation using ClientDTO (perhaps without password handling)
            Client client = new Client();
            client.setFirstName(clientDTO.getFullName());
            client.setEmail(clientDTO.getEmail());
            client.setPhone(clientDTO.getPhone());
            client.setGender(clientDTO.getGender());
            em.persist(client);
            em.getTransaction().commit();
            return new ClientDTO(client);
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