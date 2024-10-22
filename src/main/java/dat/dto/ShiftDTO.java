package dat.dto;

import dat.enums.Weekday;
import lombok.*;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShiftDTO {

    private Long id;
    private Weekday weekday;  // Enum to represent the day of the week
    private LocalTime startTime;  // Start time of the shift
    private LocalTime endTime;  // End time of the shift
    private Long veterinarianId;  // ID of the veterinarian

    private String veterinarianName; // The name of the veterinarian
}