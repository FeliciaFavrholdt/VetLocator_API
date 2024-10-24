package dat.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VeterinarianDTO {

    private Integer id;           // Unique identifier
    private String name;       // Name of the veterinarian


}