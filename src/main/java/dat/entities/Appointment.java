package dat.entities;

import dat.dto.AppointmentDTO;
import dat.enums.AppointmentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @NotNull(message = "Appointment date and time are required")
    @Column(name = "appointment_datetime", nullable = false)
    private LocalDateTime appointmentDateTime;

    @NotBlank(message = "Reason is required")
    @Column(name = "reason", nullable = false, length = 255)
    private String reason;

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AppointmentStatus status;

    @NotNull(message = "Clinic is required")
    @ManyToOne
    @JoinColumn(name = "clinic_id", nullable = false)
    private Clinic clinic;

    @NotNull(message = "Client is required")
    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @NotNull(message = "Animal is required")
    @ManyToOne
    @JoinColumn(name = "animal_id", nullable = false)
    private Animal animal;

    @NotNull(message = "Veterinarian is required")
    @ManyToOne
    @JoinColumn(name = "veterinarian_id", nullable = false)
    private Veterinarian veterinarian;

    // Method to convert from DTO
    public void convertFromDTO(AppointmentDTO appointmentDTO) {
        this.appointmentDateTime = appointmentDTO.getAppointmentDateTime();  // Corrected to match the field in DTO
        this.reason = appointmentDTO.getReason();
        this.status = AppointmentStatus.valueOf(appointmentDTO.getStatus());
        // Clinic, Client, Animal, and Veterinarian should be set in the DAO or service layer
    }
}