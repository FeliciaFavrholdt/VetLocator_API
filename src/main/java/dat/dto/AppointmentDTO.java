package dat.dtos;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDTO {

    private Long id;          // Unique identifier

    private String date;       // Date of the appointment
    private String time;       // Time of the appointment
    private String reason;     // Reason for the appointment
    private String status;     // Status of the appointment

    private Long veterinarianId;  // Veterinarian assigned to the appointment
    private String veterinarianName;  // Veterinarian name (optional)

    private Long userId;     // Person who made the appointment
    private String userName; // Person name (optional)

    private Long animalId;     // Pet that the appointment is for
    private String animalName; // Pet name (optional)
}