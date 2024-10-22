package dat.entities;

import dat.enums.Gender;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

@Entity
@Data
@Builder
@ToString(exclude = "animals")  // Avoid recursive printing of relationships
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")  // Avoid reserved keywords
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "username", nullable = false, unique = true, length = 50)
    @NotBlank(message = "Username cannot be blank")
    @Size(max = 50, message = "Username must be less than or equal to 50 characters")
    private String username;

    @Column(name = "password", nullable = false, length = 255)
    @NotBlank(message = "Password cannot be blank")
    private String password;  // Consider applying hashing in the service layer

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
    @Pattern(regexp = "\\+\\d{1,4} \\d{2,4} \\d{2,4} \\d{2,4}", message = "Phone must be a valid format (e.g., +45 XX XX XX XX)")
    private String phone;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Animal> animals;

}