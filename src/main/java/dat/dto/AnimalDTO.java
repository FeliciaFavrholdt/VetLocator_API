package dat.dto;

import dat.entities.Animal;
import dat.enums.Animals;
import dat.enums.MedicalHistory;  // Import MedicalHistory Enum
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnimalDTO {

    private Long id;
    private String name;
    private String species;
    private String breed;
    private Integer age;
    private Long ownerId;  // Reference to the owner (client)
    private String medicalHistory;  // MedicalHistory as String to handle enum conversion

    // Constructor to convert from Animal entity to AnimalDTO
    public AnimalDTO(Animal animal) {
        this.id = animal.getId();
        this.name = animal.getName();
        this.species = animal.getSpecies() != null ? animal.getSpecies().name() : null;  // Enum to String
        this.breed = animal.getBreed();
        this.age = animal.getAge();
        this.ownerId = animal.getOwner() != null ? animal.getOwner().getId() : null;
        this.medicalHistory = animal.getMedicalHistory() != null ? animal.getMedicalHistory().name() : null;  // Enum to String
    }

    // Method to convert from DTO to Entity
    public Animal toEntity() {
        Animal animal = new Animal();
        animal.setId(this.id);
        animal.setName(this.name);
        animal.setSpecies(this.species != null ? Animals.valueOf(this.species) : null);  // String to Enum
        animal.setBreed(this.breed);
        animal.setAge(this.age);
        animal.setMedicalHistory(this.medicalHistory != null ? MedicalHistory.valueOf(this.medicalHistory) : null);  // String to Enum
        return animal;
    }

    // Getter for medicalHistory
    public String getMedicalHistory() {
        return medicalHistory;
    }

    // Setter for medicalHistory
    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }
}
