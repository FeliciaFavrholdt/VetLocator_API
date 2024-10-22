package dat.dao.impl;

import dat.dao.IDAO;
import dat.dto.AnimalDTO;
import dat.entities.Animal;
import dat.entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class AnimalDAO implements IDAO<AnimalDTO, Integer> {

    private static AnimalDAO instance;
    private static EntityManagerFactory emf;

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

            // Find and set the User entity based on userId in the DTO
            User user = em.find(User.class, animalDTO.getUserId());
            animal.setUser(user);  // Set the User relationship

            // Persist the animal entity
            em.persist(animal);
            em.getTransaction().commit();

            // Return the persisted entity as a DTO
            return new AnimalDTO(animal);
        } finally {
            em.close();
        }
    }

    @Override
    public AnimalDTO read(Integer id) {
        EntityManager em = emf.createEntityManager();
        try {
            Animal animal = em.find(Animal.class, id);
            return animal != null ? new AnimalDTO(animal) : null;
        } finally {
            em.close();
        }
    }

    @Override
    public List<AnimalDTO> readAll() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<AnimalDTO> query = em.createQuery("SELECT new dat.dto.AnimalDTO(a) FROM Animal a", AnimalDTO.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public AnimalDTO update(Integer id, AnimalDTO animalDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            // Find the existing animal by ID
            Animal animal = em.find(Animal.class, id);
            if (animal != null) {
                // Update the entity with the new DTO data
                animal.updateFromDTO(animalDTO);

                // Optionally, update the User relationship based on userId in DTO
                if (!animal.getUser().getId().equals(animalDTO.getUserId())) {
                    User user = em.find(User.class, animalDTO.getUserId());
                    animal.setUser(user);
                }

                // Merge and commit the changes
                Animal mergedAnimal = em.merge(animal);
                em.getTransaction().commit();

                return new AnimalDTO(mergedAnimal);
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
            Animal animal = em.find(Animal.class, id);
            if (animal != null) {
                em.remove(animal);
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
            Animal animal = em.find(Animal.class, id);
            return animal != null;
        } finally {
            em.close();
        }
    }
}
