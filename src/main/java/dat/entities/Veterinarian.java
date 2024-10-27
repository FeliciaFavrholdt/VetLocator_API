package dat.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "veterinarians")
public class Veterinarian {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String specialties;  // This can be a comma-separated string or a one-to-many relationship

    @ManyToOne
    @JoinColumn(name = "clinic_id")
    private Clinic clinic;

    private Boolean availableForEmergency;
}
