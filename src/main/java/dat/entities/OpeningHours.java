package dat.entities;

import jakarta.persistence.*;
import lombok.Data;
import dat.enums.Weekday;

@Data
@Entity
@Table(name = "opening_hours")
public class OpeningHours {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "clinic_id", nullable = false)
    private Clinic clinic;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false, length = 10)
    private Weekday dayOfWeek;

    @Column(name = "opening_time", nullable = false, length = 5)
    private String openingTime;

    @Column(name = "closing_time", nullable = false, length = 5)
    private String closingTime;
}