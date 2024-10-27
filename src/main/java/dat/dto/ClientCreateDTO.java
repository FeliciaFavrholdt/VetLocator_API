package dat.dto;

import dat.entities.Client;
import dat.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientCreateDTO {

    private String firstName;   // Now split full name into first and last name
    private String lastName;
    private String email;
    private String phone;
    private Gender gender;
    private String username;    // Include username only for creation
    private String password;    // Include password only for creation

    // Optionally you can include the animals if needed during creation
    private Set<AnimalDTO> animals;  // A set of AnimalDTOs to represent user's pets

    // If you want a constructor from the Client entity for creation, use it as needed
    public ClientCreateDTO(Client client) {
        this.firstName = client.getFirstName();
        this.lastName = client.getLastName();
        this.email = client.getEmail();
        this.phone = client.getPhone();
        this.gender = client.getGender();
        this.username = client.getUsername();
        if (client.getAnimals() != null) {
            this.animals = client.getAnimals().stream()
                    .map(AnimalDTO::new)
                    .collect(Collectors.toSet());
        }
    }
}
