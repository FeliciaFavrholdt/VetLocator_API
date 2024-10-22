package dat.entities;

import dat.enums.Weekday;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpeningHours {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Enum to represent the day of the week
    @Enumerated(EnumType.STRING)
    private Weekday weekday;

    // Start and end times for the shift
    private LocalTime startTime;
    private LocalTime endTime;

    // Many-to-One relationship with Veterinarian
    @ManyToOne
    @JoinColumn(name = "veterinarian_id", nullable = false)
    private VeterinaryClinic veterinaryClinic;

    // Method to check if a vet is currently on duty for the given day and time
    public boolean isOnDuty(LocalTime currentTime, Weekday currentWeekday) {
        return this.weekday == currentWeekday &&
                currentTime.isAfter(startTime) && currentTime.isBefore(endTime);
    }
}