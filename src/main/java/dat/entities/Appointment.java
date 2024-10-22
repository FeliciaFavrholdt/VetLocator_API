package dat.entities;

import dat.enums.AppointmentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"veterinaryClinic", "user", "animal"})  // Exclude relationships from toString to avoid recursion
@Table(name = "appointments", indexes = {
        @Index(name = "idx_veterinarian_id", columnList = "veterinarian_id"),
        @Index(name = "idx_user_id", columnList = "user_id"),
        @Index(name = "idx_animal_id", columnList = "animal_id")
})
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;  // Unique identifier

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "time", nullable = false)
    private LocalTime time;

    @Column(name = "reason", nullable = false, length = 255)
    @NotBlank(message = "Reason cannot be blank")
    @Size(max = 255, message = "Reason must be less than or equal to 255 characters")
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AppointmentStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "veterinarian_id", nullable = false)
    private Clinic veterinaryClinic;  // Veterinarian assigned to the appointment

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Client client;  // Person who made the appointment

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "animal_id", nullable = false)
    private Animal animal;  // Pet for which the appointment is scheduled
}
