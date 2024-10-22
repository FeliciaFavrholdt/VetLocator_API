package dat.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnimalDTO {

    private Long id;
    private String name;
    private String species;
    private int age;
    private Long userId;  // Reference to the owner (user)

}