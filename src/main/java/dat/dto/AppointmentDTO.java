package dat.dto;

import dat.enums.AppointmentStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDTO {

    private Integer id;           // Unique identifier for the appointment

    private LocalDate date;       // Date of the appointment
    private LocalTime time;       // Time of the appointment
    private String reason;        // Reason for the appointment
    private AppointmentStatus status;  // Status of the appointment (enum)

    private Integer veterinarianId;    // ID of the veterinarian (Clinic entity in this case)
    private String veterinarianName;   // Veterinarian's name (optional for display purposes)

    private Integer clientId;      // ID of the client who made the appointment
    private String clientName;     // Client's name (optional for display purposes)

    private Integer animalId;      // ID of the pet associated with the appointment
    private String animalName;     // Pet's name (optional for display purposes)
}
