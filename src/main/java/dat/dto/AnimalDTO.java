package dat.dto;

import dat.entities.Animal;
import dat.enums.Animals;
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
    private Integer age;
    private Long ownerId;  // Reference to the owner (client)

    // Constructor to convert from Animal entity to AnimalDTO
    public AnimalDTO(Animal animal) {
        this.id = animal.getId();
        this.name = animal.getName();
        this.species = animal.getSpecies() != null ? animal.getSpecies().name() : null;  // Enum to String
        this.age = animal.getAge();
        this.ownerId = animal.getOwner() != null ? animal.getOwner().getId() : null;
    }

    // Method to convert from DTO to Entity
    public Animal toEntity() {
        Animal animal = new Animal();
        animal.setId(this.id);
        animal.setName(this.name);
        animal.setSpecies(this.species != null ? Animals.valueOf(this.species) : null);  // String to Enum
        animal.setAge(this.age);
        return animal;
    }
}

