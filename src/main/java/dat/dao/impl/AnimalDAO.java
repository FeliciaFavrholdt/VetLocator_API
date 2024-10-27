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

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class AnimalDAO implements IDAO<AnimalDTO, Integer> {

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
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            Animal animal = animalDTO.toEntity();
            Client client = em.find(Client.class, animalDTO.getOwnerId());
            if (client == null) {
                throw new JpaException(400, "Client not found for userId: " + animalDTO.getOwnerId());
            }
            animal.setOwner(client);

            em.persist(animal);
            em.getTransaction().commit();

            return new AnimalDTO(animal);
        } catch (PersistenceException e) {
            throw new JpaException(500, "Error creating animal in the database.");
        }
    }

    @Override
    public AnimalDTO read(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            Animal animal = em.find(Animal.class, id);
            if (animal == null) {
                throw new JpaException(404, "Animal not found for ID: " + id);
            }
            return new AnimalDTO(animal);
        }
    }

    @Override
    public List<AnimalDTO> readAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<AnimalDTO> query = em.createQuery("SELECT new dat.dto.AnimalDTO(a) FROM Animal a", AnimalDTO.class);
            return query.getResultList();
        }
    }

    @Override
    public AnimalDTO update(Integer id, AnimalDTO animalDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            Animal animal = em.find(Animal.class, id);
            if (animal == null) {
                throw new JpaException(404, "Animal not found for ID: " + id);
            }
            animal.convertFromDTO(animalDTO);

            if (!animal.getOwner().getId().equals(animalDTO.getOwnerId())) {
                Client client = em.find(Client.class, animalDTO.getOwnerId());
                if (client == null) {
                    throw new JpaException(400, "Client not found for userId: " + animalDTO.getOwnerId());
                }
                animal.setOwner(client);
            }

            Animal mergedAnimal = em.merge(animal);
            em.getTransaction().commit();

            return new AnimalDTO(mergedAnimal);
        } catch (PersistenceException e) {
            throw new JpaException(500, "Error updating animal in the database.");
        }
    }

    @Override
    public void delete(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Animal animal = em.find(Animal.class, id);
            if (animal == null) {
                throw new JpaException(404, "Animal not found for ID: " + id);
            }
            em.remove(animal);
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            throw new JpaException(500, "Error deleting animal from the database.");
        }
    }

    @Override
    public boolean validatePrimaryKey(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(Animal.class, id) != null;
        }
    }
}