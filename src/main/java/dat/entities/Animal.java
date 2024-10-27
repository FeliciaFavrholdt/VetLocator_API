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

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private Client owner;

    @Enumerated(EnumType.STRING)
    @Column(name = "medical_history", nullable = false, length = 50)
    private MedicalHistory medicalHistory;

    public void convertFromDTO(AnimalDTO animalDTO) {
        this.name = animalDTO.getName();
        this.species = Animals.valueOf(animalDTO.getSpecies());
        this.age = animalDTO.getAge();
    }
}