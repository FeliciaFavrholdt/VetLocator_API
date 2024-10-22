package dat.entities;

import dat.enums.Weekday;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "veterinaryClinic")  // Exclude relationship from toString to avoid recursion
@Table(name = "opening_hours")
public class OpeningHours {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    // Enum to represent the day of the week
    @Enumerated(EnumType.STRING)
    @Column(name = "weekday", nullable = false)
    @NotNull(message = "Weekday cannot be null")
    private Weekday weekday;

    // Start and end times for the shift
    @Column(name = "start_time", nullable = false)
    @NotNull(message = "Start time cannot be null")
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    @NotNull(message = "End time cannot be null")
    private LocalTime endTime;

    // Many-to-One relationship with VeterinaryClinic, lazy load to improve performance
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "veterinary_clinic_id", nullable = false)
    @NotNull(message = "Veterinary clinic cannot be null")
    private VeterinaryClinic veterinaryClinic;

    // Method to check if a vet is currently on duty for the given day and time
    public boolean isOnDuty(LocalTime currentTime, Weekday currentWeekday) {
        if (this.weekday != currentWeekday) {
            return false;
        }
        return !currentTime.isBefore(startTime) && !currentTime.isAfter(endTime);
    }
}