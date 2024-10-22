package dat.dto;

import dat.entities.User;
import lombok.*;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Integer id;
    private String fullName;  // This will be firstName + lastName
    private String email;
    private String phone;
    private Set<AnimalDTO> animals;  // A set of AnimalDTOs to represent user's pets

    // Constructor to map from User entity to UserDTO
    public UserDTO(User user) {
        this.id = user.getId();
        this.fullName = user.getFirstName() + " " + user.getLastName();  // Combine first and last name
        this.email = user.getEmail();
        this.phone = user.getPhone();
        if (user.getAnimals() != null) {
            this.animals = user.getAnimals().stream()
                    .map(AnimalDTO::new)  // Convert Animal entities to AnimalDTOs
                    .collect(Collectors.toSet());
        }
    }
}
