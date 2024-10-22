package dat.entities;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;          // Unique identifier


    private String date;       // Date of the appointment
    private String time;       // Time of the appointment
    private String reason;     // Reason for the appointment
    private String status;     // Status of the appointment

    @ManyToOne
    @JoinColumn(name = "veterinarian_id", nullable = false)  // Foreign key to the Veterinarian table
    private VeterinaryClinic veterinaryClinic;  // Veterinarian assigned to the appointment

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)  // Foreign key to the Person table
    private User user;     // Person who made the appointment

    @ManyToOne
    @JoinColumn(name = "animal_id", nullable = false)  // Foreign key to the Animal table
    private Animal animal;           // Pet that the appointment is for
}