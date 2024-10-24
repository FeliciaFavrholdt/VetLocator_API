package dat.dto;

import dat.entities.Animal;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnimalDTO {

    private Integer id;
    private String name;
    private String species;
    private int age;
    private Integer userId;  // Reference to the owner (user)

    // Constructor to convert from Animal entity to AnimalDTO
    public AnimalDTO(Animal animal) {
        this.id = animal.getId();
        this.name = animal.getName();
        this.species = animal.getSpecies();
        this.age = animal.getAge();
        this.userId = animal.getClient() != null ? animal.getClient().getId() : null;
    }
}
