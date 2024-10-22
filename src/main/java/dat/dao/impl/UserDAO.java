package dat.dao.impl;

import dat.dao.IDAO;
import dat.dto.UserDTO;
import dat.entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class UserDAO implements IDAO<UserDTO, Integer> {

    private static UserDAO instance;
    private static EntityManagerFactory emf;

    public static UserDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new UserDAO();
        }
        return instance;
    }

    @Override
    public UserDTO create(UserDTO userDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            User user = new User(userDTO);
            em.persist(user);
            em.getTransaction().commit();
            return new UserDTO(user);
        }
    }

    @Override
    public UserDTO read(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            User user = em.find(User.class, id);
            return user != null ? new UserDTO(user) : null;
        }
    }

    @Override
    public List<UserDTO> readAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<UserDTO> query = em.createQuery("SELECT new dat.dto.UserDTO(u) FROM User u", UserDTO.class);
            return query.getResultList();
        }
    }

    @Override
    public UserDTO update(Integer id, UserDTO userDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            User user = em.find(User.class, id);
            if (user != null) {
                user.updateFromDTO(userDTO);
                User mergedUser = em.merge(user);
                em.getTransaction().commit();
                return new UserDTO(mergedUser);
            }
            return null;
        }
    }

    @Override
    public void delete(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            User user = em.find(User.class, id);
            if (user != null) {
                em.remove(user);
            }
            em.getTransaction().commit();
        }
    }

    @Override
    public boolean validatePrimaryKey(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            User user = em.find(User.class, id);
            return user != null;
        }
    }
}