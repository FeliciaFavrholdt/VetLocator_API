package dat.dao.impl;

import dat.dao.IDAO;
import dat.dto.AnimalDTO;
import dat.entities.Animal;
import dat.entities.Client;
import dat.exception.JpaException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.PersistenceException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class AnimalDAO implements IDAO<AnimalDTO, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(AnimalDAO.class);  // Logger instance
    private static AnimalDAO instance;
    private static EntityManagerFactory emf;

    public AnimalDAO(EntityManagerFactory emf) {
        AnimalDAO.emf = emf;
    }

    public static AnimalDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new AnimalDAO();
        }
        return instance;
    }

    @Override
    public AnimalDTO create(AnimalDTO animalDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Animal animal = animalDTO.toEntity();
            Client client = em.find(Client.class, animalDTO.getOwnerId());

            if (client == null) {
                logger.warn("Client not found for userId: {}", animalDTO.getOwnerId());
                throw new JpaException(400, "Client not found for userId: " + animalDTO.getOwnerId());
            }

            animal.setOwner(client);
            em.persist(animal);
            em.getTransaction().commit();

            logger.info("Animal created successfully with ID {}", animal.getId());
            return new AnimalDTO(animal);
        } catch (PersistenceException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            logger.error("Error creating animal in the database: {}", e.getMessage());
            throw new JpaException(500, "Error creating animal in the database.");
        } finally {
            em.close();
        }
    }

    @Override
    public AnimalDTO read(Integer id) {
        EntityManager em = emf.createEntityManager();
        try {
            Animal animal = em.find(Animal.class, id);
            if (animal == null) {
                logger.warn("Animal not found for ID: {}", id);
                throw new JpaException(404, "Animal not found for ID: " + id);
            }
            logger.info("Animal with ID {} successfully retrieved.", id);
            return new AnimalDTO(animal);
        } finally {
            em.close();
        }
    }

    @Override
    public List<AnimalDTO> readAll() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<AnimalDTO> query = em.createQuery("SELECT new dat.dto.AnimalDTO(a) FROM Animal a", AnimalDTO.class);
            List<AnimalDTO> animals = query.getResultList();
            logger.info("Successfully retrieved {} animals.", animals.size());
            return animals;
        } finally {
            em.close();
        }
    }

    @Override
    public AnimalDTO update(Integer id, AnimalDTO animalDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            Animal animal = em.find(Animal.class, id);
            if (animal == null) {
                logger.warn("Animal not found for ID: {}", id);
                throw new JpaException(404, "Animal not found for ID: " + id);
            }

            animal.convertFromDTO(animalDTO);

            if (!animal.getOwner().getId().equals(animalDTO.getOwnerId())) {
                Client client = em.find(Client.class, animalDTO.getOwnerId());
                if (client == null) {
                    logger.warn("Client not found for userId: {}", animalDTO.getOwnerId());
                    throw new JpaException(400, "Client not found for userId: " + animalDTO.getOwnerId());
                }
                animal.setOwner(client);
            }

            Animal mergedAnimal = em.merge(animal);
            em.getTransaction().commit();
            logger.info("Animal with ID {} successfully updated.", id);
            return new AnimalDTO(mergedAnimal);
        } catch (PersistenceException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            logger.error("Error updating animal in the database: {}", e.getMessage());
            throw new JpaException(500, "Error updating animal in the database.");
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(Integer id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Animal animal = em.find(Animal.class, id);
            if (animal == null) {
                logger.warn("Animal not found for ID: {}", id);
                throw new JpaException(404, "Animal not found for ID: " + id);
            }
            em.remove(animal);
            em.getTransaction().commit();
            logger.info("Animal with ID {} successfully deleted.", id);
        } catch (PersistenceException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            logger.error("Error deleting animal from the database: {}", e.getMessage());
            throw new JpaException(500, "Error deleting animal from the database.");
        } finally {
            em.close();
        }
    }

    @Override
    public boolean validatePrimaryKey(Integer id) {
        EntityManager em = emf.createEntityManager();
        try {
            boolean isValid = em.find(Animal.class, id) != null;
            if (!isValid) {
                logger.warn("Invalid primary key: {}", id);
            }
            return isValid;
        } finally {
            em.close();
        }
    }
}