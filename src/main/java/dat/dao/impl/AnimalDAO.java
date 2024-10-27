package dat.dao.impl;

import dat.dao.IDAO;
import dat.dto.AnimalDTO;
import dat.entities.Animal;
import dat.entities.Client;
import dat.exceptions.JpaException;
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

    private static final Logger logger = LoggerFactory.getLogger(AnimalDAO.class);

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

            // Convert DTO to entity
            Animal animal = new Animal(animalDTO);

            // Find and set the Client entity based on userId in the DTO
            Client client = em.find(Client.class, animalDTO.getUserId());
            if (client == null) {
                throw new JpaException(400, "Client not found for userId: " + animalDTO.getUserId());
            }
            animal.setClient(client);

            // Persist the animal entity
            em.persist(animal);
            em.getTransaction().commit();

            // Return the persisted entity as a DTO
            return new AnimalDTO(animal);
        } catch (PersistenceException e) {
            em.getTransaction().rollback();
            logger.error("Error creating animal: {}", e.getMessage(), e);
            throw new JpaException(500, "Error creating animal in the database.");
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public AnimalDTO read(Integer id) {
        EntityManager em = emf.createEntityManager();
        try {
            Animal animal = em.find(Animal.class, id);
            if (animal == null) {
                throw new JpaException(404, "Animal not found for ID: " + id);
            }
            return new AnimalDTO(animal);
        } catch (PersistenceException e) {
            logger.error("Error reading animal with ID {}: {}", id, e.getMessage(), e);
            throw new JpaException(500, "Error reading animal from the database.");
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public List<AnimalDTO> readAll() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<AnimalDTO> query = em.createQuery("SELECT new dat.dto.AnimalDTO(a) FROM Animal a", AnimalDTO.class);
            return query.getResultList();
        } catch (PersistenceException e) {
            logger.error("Error fetching all animals: {}", e.getMessage(), e);
            throw new JpaException(500, "Error fetching animals from the database.");
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public AnimalDTO update(Integer id, AnimalDTO animalDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            // Find the existing animal by ID
            Animal animal = em.find(Animal.class, id);
            if (animal == null) {
                throw new JpaException(404, "Animal not found for ID: " + id);
            }

            // Update the entity with the new DTO data
            animal.updateFromDTO(animalDTO);

            // Optionally, update the Client relationship based on userId in DTO
            if (!animal.getClient().getId().equals(animalDTO.getUserId())) {
                Client client = em.find(Client.class, animalDTO.getUserId());
                if (client == null) {
                    throw new JpaException(400, "Client not found for userId: " + animalDTO.getUserId());
                }
                animal.setClient(client);
            }

            // Merge and commit the changes
            Animal mergedAnimal = em.merge(animal);
            em.getTransaction().commit();

            return new AnimalDTO(mergedAnimal);
        } catch (PersistenceException e) {
            em.getTransaction().rollback();
            logger.error("Error updating animal with ID {}: {}", id, e.getMessage(), e);
            throw new JpaException(500, "Error updating animal in the database.");
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
            Animal animal = em.find(Animal.class, id);
            if (animal == null) {
                throw new JpaException(404, "Animal not found for ID: " + id);
            }
            em.remove(animal);
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            em.getTransaction().rollback();
            logger.error("Error deleting animal with ID {}: {}", id, e.getMessage(), e);
            throw new JpaException(500, "Error deleting animal from the database.");
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
            Animal animal = em.find(Animal.class, id);
            return animal != null;
        } catch (PersistenceException e) {
            logger.error("Error validating animal primary key with ID {}: {}", id, e.getMessage(), e);
            throw new JpaException(500, "Error validating animal primary key.");
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }
}