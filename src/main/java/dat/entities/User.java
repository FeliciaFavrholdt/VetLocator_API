package dat.entities;

import dat.dto.AnimalDTO;
import dat.dto.UserDTO;
import dat.enums.Gender;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@Builder
//@ToString(exclude = "animals")
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;

    @Column(name = "username", nullable = false, unique = true, length = 50)
    @NotBlank(message = "Username cannot be blank")
    @Size(max = 50, message = "Username must be less than or equal to 50 characters")
    private String username;

    @Column(name = "password", nullable = false, length = 255)
    @NotBlank(message = "Password cannot be blank")
    private String password;

    @Column(name = "first_name", nullable = false, length = 15)
    @NotBlank(message = "First name cannot be blank")
    @Size(max = 15, message = "First name must be less than or equal to 15 characters")
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    @NotBlank(message = "Last name cannot be blank")
    @Size(max = 50, message = "Last name must be less than or equal to 50 characters")
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email cannot be blank")
    @Size(max = 100, message = "Email must be less than or equal to 100 characters")
    @Column(name = "email", nullable = false, length = 100, unique = true)
    private String email;

    @Column(name = "phone", nullable = false, length = 20)
    @NotBlank(message = "Phone cannot be blank")
    @Pattern(regexp = "\\+\\d{1,4} \\d{2} \\d{2} \\d{2} \\d{2}", message = "Phone must be a valid format (e.g., +45 XX XX XX XX)")
    private String phone;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Animal> animals = new HashSet<>();  // Initialize with empty set

    public User(String username, String password, String firstName, String lastName, String email, String phone) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
    }

    public void addAnimal(Animal animal) {
        animals.add(animal);
        animal.setUser(this);  // Ensure bidirectional relationship
    }

    public void removeAnimal(Animal animal) {
        animals.remove(animal);
        animal.setUser(null);
    }

    public User(UserDTO dto) {
        this.username = dto.getFullName().toLowerCase().replaceAll(" ", "_");
        this.firstName = dto.getFullName().split(" ")[0];
        this.lastName = dto.getFullName().split(" ")[1];
        this.email = dto.getEmail();
        this.phone = dto.getPhone();
        if (dto.getAnimals() != null) {
            this.animals = dto.getAnimals().stream().map(Animal::new).collect(Collectors.toSet());
            this.animals.forEach(animal -> animal.setUser(this));  // Ensure bidirectional association
        }
    }

    public void updateFromDTO(UserDTO dto) {
        this.firstName = dto.getFullName().split(" ")[0];
        this.lastName = dto.getFullName().split(" ")[1];
        this.email = dto.getEmail();
        this.phone = dto.getPhone();
        if (dto.getAnimals() != null) {
            this.animals = dto.getAnimals().stream().map(Animal::new).collect(Collectors.toSet());
            this.animals.forEach(animal -> animal.setUser(this));  // Ensure bidirectional association
        }
    }
}

