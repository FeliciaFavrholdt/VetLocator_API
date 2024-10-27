package dat.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "opening_hours")
public class OpeningHours {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "clinic_id")
    private Clinic clinic;

    private String dayOfWeek;
    private String openingTime;
    private String closingTime;
}
