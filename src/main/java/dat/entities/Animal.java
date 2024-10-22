package dat.entities;

import dat.dto.AnimalDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
//@ToString(exclude = "user")  // Avoid recursive toString calls
@Table(name = "animals")
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;

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
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Constructor to create an Animal from AnimalDTO
    public Animal(AnimalDTO dto) {
        this.id = dto.getId();
        this.name = dto.getName();
        this.species = dto.getSpecies();
        this.age = dto.getAge();
        // User will be set after fetching from the database
    }

    public void setUser(User user) {
        this.user = user;
        if (!user.getAnimals().contains(this)) {
            user.getAnimals().add(this);  // Add this animal to the user's set of animals if it's not already present
        }
    }

    public void updateFromDTO(AnimalDTO dto) {
        this.name = dto.getName();
        this.species = dto.getSpecies();
        this.age = dto.getAge();
    }
}