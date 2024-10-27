package dat.security.dao;

import dat.security.entities.Role;
import dat.security.entities.User;
import jakarta.validation.ValidationException;

public interface ISecurityDAO {
    User getVerifiedUser(String username, String password) throws ValidationException;
    User createUser(String username, String password);
    Role createRole(String role);
    User addUserRole(String username, String role);
}
