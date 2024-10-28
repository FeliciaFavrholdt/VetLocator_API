package dat.dto;

import dat.entities.Animal;
import dat.entities.Appointment;
import dat.entities.Clinic;
import dat.entities.Client;
import dat.entities.Veterinarian;
import dat.enums.AppointmentStatus;
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
    private LocalDateTime appointmentDateTime;
    private String reason;
    private String status;
    private Long clinicId;
    private Long clientId;
    private Long animalId;
    private Long veterinarianId;

    public AppointmentDTO(Appointment appointment) {
        this.id = appointment.getId();
        this.appointmentDateTime = appointment.getAppointmentDateTime();
        this.reason = appointment.getReason();
        this.status = appointment.getStatus().name();
        this.clinicId = appointment.getClinic().getId();
        this.clientId = appointment.getClient().getId();
        this.animalId = appointment.getAnimal().getId();
        this.veterinarianId = appointment.getVeterinarian().getId();
    }

    public Appointment toEntity(Clinic clinic, Client client, Animal animal, Veterinarian veterinarian) {
        Appointment appointment = new Appointment();
        appointment.setId(this.id);
        appointment.setAppointmentDateTime(this.appointmentDateTime);
        appointment.setReason(this.reason);
        appointment.setStatus(AppointmentStatus.valueOf(this.status));
        appointment.setClinic(clinic);
        appointment.setClient(client);
        appointment.setAnimal(animal);
        appointment.setVeterinarian(veterinarian);
        return appointment;
    }
}