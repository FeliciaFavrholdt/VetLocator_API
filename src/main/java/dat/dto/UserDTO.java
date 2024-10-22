package dat.dto;

import lombok.*;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private Set<AnimalDTO> animals;  // A set of AnimalDTOs to represent user's pets

}