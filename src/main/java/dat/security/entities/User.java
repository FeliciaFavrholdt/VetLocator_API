package dat.security.entities;

import jakarta.persistence.*;
import org.mindrot.jbcrypt.BCrypt;
import java.util.HashSet;
import java.util.Set;

@Entity
public class User implements ISecurityUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles = new HashSet<>();

    // Constructor to hash the password
    public User(String username, String password) {
        this.username = username;
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
    }

    @Override
    public Set<String> getRolesAsStrings() {
        Set<String> roleStrings = new HashSet<>();
        for (Role role : roles) {
            roleStrings.add(role.getRoleName());
        }
        return roleStrings;
    }

    @Override
    public boolean verifyPassword(String pw) {
        return BCrypt.checkpw(pw, this.password);
    }

    @Override
    public void addRole(Role role) {
        roles.add(role);
    }

    @Override
    public void removeRole(String roleName) {
        roles.removeIf(role -> role.getRoleName().equals(roleName));
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public Set<Role> getRoles() {
        return roles;
    }
}
