package dat.dto;

import dat.entities.Appointment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDTO {

    private Long id;
    private LocalDateTime appointmentTime;
    private String reason;
    private String status;
    private Long animalId;
    private Long veterinarianId;
    private Long clinicId;

    // Constructor to convert from Appointment entity to AppointmentDTO
    public AppointmentDTO(Appointment appointment) {
        this.id = appointment.getId();
        this.appointmentTime = appointment.getAppointmentTime();
        this.reason = appointment.getReason();
        this.status = appointment.getStatus().toString(); // Assuming status is an enum
        this.animalId = appointment.getAnimal() != null ? appointment.getAnimal().getId() : null;
        this.veterinarianId = appointment.getVeterinarian() != null ? appointment.getVeterinarian().getId() : null;
        this.clinicId = appointment.getClinic() != null ? appointment.getClinic().getId() : null;
    }

    // Method to convert from DTO to Entity
    public Appointment toEntity() {
        Appointment appointment = new Appointment();
        appointment.setId(this.id);
        appointment.setAppointmentTime(this.appointmentTime);
        appointment.setReason(this.reason);
        appointment.setStatus(dat.enums.AppointmentStatus.valueOf(this.status));
        return appointment;
    }
}