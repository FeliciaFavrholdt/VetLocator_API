package dat.dto;

import dat.entities.Client;
import lombok.*;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientDTO {

    private Integer id;
    private String fullName;  // This will be firstName + lastName
    private String email;
    private String phone;
    private Set<AnimalDTO> animals;  // A set of AnimalDTOs to represent user's pets

    // Constructor to map from User entity to UserDTO
    public ClientDTO(Client client) {
        this.id = client.getId();
        this.fullName = client.getFirstName() + " " + client.getLastName();  // Combine first and last name
        this.email = client.getEmail();
        this.phone = client.getPhone();
        if (client.getAnimals() != null) {
            this.animals = client.getAnimals().stream()
                    .map(AnimalDTO::new)  // Convert Animal entities to AnimalDTOs
                    .collect(Collectors.toSet());
        }
    }
}
