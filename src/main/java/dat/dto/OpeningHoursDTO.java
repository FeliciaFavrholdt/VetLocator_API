package dat.dto;

import dat.enums.Weekday;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpeningHoursDTO {

    private Long id;

    @NotNull(message = "Weekday cannot be null")
    private Weekday weekday;

    @NotNull(message = "Start time cannot be null")
    private LocalTime startTime;

    @NotNull(message = "End time cannot be null")
    private LocalTime endTime;

    @NotNull(message = "Veterinary clinic ID cannot be null")
    private Long veterinaryClinicId; // We'll map the clinic ID directly for simplicity
}