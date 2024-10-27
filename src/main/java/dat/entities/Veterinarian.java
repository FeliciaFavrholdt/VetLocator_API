package dat.entities;

import jakarta.persistence.*;
import lombok.Data;
import dat.enums.Specialization;
import dat.enums.Availability;

@Data
@Entity
@Table(name = "veterinarians")
public class Veterinarian {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private Specialization specialties;

    @ManyToOne
    @JoinColumn(name = "clinic_id")
    private Clinic clinic;

    @Enumerated(EnumType.STRING)
    private Availability availableForEmergency;
}
