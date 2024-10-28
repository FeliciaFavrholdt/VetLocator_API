package dat.entities;

import dat.dto.AnimalDTO;
import dat.enums.MedicalHistory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import dat.enums.Animals;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "animals")
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "species", nullable = false, length = 50)
    private Animals species;

    @Column(name = "breed", length = 100)
    private String breed;

    @Column(name = "age")
    private Integer age;

    @ManyToOne(fetch = FetchType.LAZY)  // Optional: Lazy loading to avoid unnecessary fetches
    @JoinColumn(name = "owner_id", nullable = false)
    private Client owner;

    @Enumerated(EnumType.STRING)
    @Column(name = "medical_history", nullable = false, length = 50)
    private MedicalHistory medicalHistory;

    // Method to convert from AnimalDTO to Animal
    public void convertFromDTO(AnimalDTO animalDTO) {
        if (animalDTO == null) {
            throw new IllegalArgumentException("AnimalDTO cannot be null");
        }
        this.name = animalDTO.getName();

        if (animalDTO.getSpecies() != null) {
            this.species = Animals.valueOf(animalDTO.getSpecies());
        } else {
            this.species = null;
        }

        this.age = animalDTO.getAge();
        this.breed = animalDTO.getBreed();

        if (animalDTO.getMedicalHistory() != null) {
            this.medicalHistory = MedicalHistory.valueOf(animalDTO.getMedicalHistory());
        } else {
            this.medicalHistory = null;
        }

        // The owner is handled outside of this method since it typically involves fetching an existing entity.
    }
}
