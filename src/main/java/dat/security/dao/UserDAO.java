package dat.security.dao;

import dat.security.entities.Role;
import dat.security.entities.User;
import jakarta.persistence.*;
import jakarta.validation.ValidationException;

public class UserDAO implements ISecurityDAO {

    @PersistenceContext
    private EntityManager em;

    @Override
    public User getVerifiedUser(String username, String password) throws ValidationException {
        try {
            User user = em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                    .setParameter("username", username)
                    .getSingleResult();
            if (user.verifyPassword(password)) {
                return user;
            } else {
                throw new ValidationException("Invalid password");
            }
        } catch (NoResultException e) {
            throw new ValidationException("User not found");
        }
    }

    @Override
    public User createUser(String username, String password) {
        User user = new User(username, password);
        em.persist(user);
        return user;
    }

    @Override
    public Role createRole(String role) {
        Role newRole = new Role(role);
        em.persist(newRole);
        return newRole;
    }

    @Override
    public User addUserRole(String username, String role) {
        User user = em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                .setParameter("username", username)
                .getSingleResult();
        Role roleEntity = em.createQuery("SELECT r FROM Role r WHERE r.roleName = :roleName", Role.class)
                .setParameter("roleName", role)
                .getSingleResult();
        user.addRole(roleEntity);
        em.merge(user);
        return user;
    }
}
