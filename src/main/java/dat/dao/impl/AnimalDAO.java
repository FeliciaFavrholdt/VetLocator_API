package dat.dao.impl;

import dat.dao.IDAO;
import dat.dto.AnimalDTO;
import dat.entities.Animal;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
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
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Animal animal = new Animal(animalDTO);
            em.persist(animal);
            em.getTransaction().commit();
            return new AnimalDTO(animal);
        }
    }

    @Override
    public AnimalDTO read(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            Animal animal = em.find(Animal.class, id);
            return animal != null ? new AnimalDTO(animal) : null;
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
            if (animal != null) {
                animal.updateFromDTO(animalDTO);
                Animal mergedAnimal = em.merge(animal);
                em.getTransaction().commit();
                return new AnimalDTO(mergedAnimal);
            }
            return null;
        }
    }

    @Override
    public void delete(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Animal animal = em.find(Animal.class, id);
            if (animal != null) {
                em.remove(animal);
            }
            em.getTransaction().commit();
        }
    }

    @Override
    public boolean validatePrimaryKey(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            Animal animal = em.find(Animal.class, id);
            return animal != null;
        }
    }
}