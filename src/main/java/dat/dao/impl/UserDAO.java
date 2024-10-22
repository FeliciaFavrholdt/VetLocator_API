package dat.dao.impl;

import dat.dao.IDAO;
import dat.dto.UserDTO;
import dat.entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class UserDAO implements IDAO<UserDTO, Integer> {

    public static UserDAO instance;
    public static EntityManagerFactory emf;

    public static UserDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new UserDAO();
        }
        return instance;
    }

    @Override
    public UserDTO create(UserDTO userDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            // Convert DTO to User entity
            User user = new User(userDTO);
            em.persist(user);
            em.getTransaction().commit();
            // Return the persisted entity as a DTO
            return new UserDTO(user);
        } finally {
            em.close();
        }
    }

    @Override
    public UserDTO read(Integer id) {
        EntityManager em = emf.createEntityManager();
        try {
            User user = em.find(User.class, id);
            return user != null ? new UserDTO(user) : null;
        } finally {
            em.close();
        }
    }

    @Override
    public List<UserDTO> readAll() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<UserDTO> query = em.createQuery("SELECT new dat.dto.UserDTO(u) FROM User u", UserDTO.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public UserDTO update(Integer id, UserDTO userDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            User user = em.find(User.class, id);
            if (user != null) {
                // Update the entity with the new DTO data
                user.updateFromDTO(userDTO);
                User mergedUser = em.merge(user);
                em.getTransaction().commit();
                return new UserDTO(mergedUser);
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
            User user = em.find(User.class, id);
            if (user != null) {
                em.remove(user);
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
            User user = em.find(User.class, id);
            return user != null;
        } finally {
            em.close();
        }
    }
}
