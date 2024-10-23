package dat.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VeterinarianDTO {

    private Integer id;           // Unique identifier
    private String name;       // Name of the veterinarian
    private String specialty;  // Veterinarian's specialty
    private String phone;      // Contact phone number
    private String email;      // Contact email
    private String address;    // Address of the veterinarian
}