package dat.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "user")  // Avoid recursive toString calls
@Table(name = "animals")
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    @NotBlank(message = "Name cannot be blank")
    @Size(max = 50, message = "Name must be less than or equal to 50 characters")
    private String name;

    @Column(name = "species", nullable = false, length = 50)
    @NotBlank(message = "Species cannot be blank")
    @Size(max = 50, message = "Species must be less than or equal to 50 characters")
    private String species;

    @Column(name = "age", nullable = false)
    @Min(value = 0, message = "Age must be zero or greater")
    private int age;

    // Many-to-One relationship with User (owner)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)  // Foreign key to the User table
    private User user;
}