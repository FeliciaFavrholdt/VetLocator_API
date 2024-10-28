package dat.dto;

import dat.entities.OpeningHours;
import dat.enums.Weekday;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpeningHoursDTO {

    private Long id;
    private Weekday weekday;
    private LocalTime openTime;
    private LocalTime closeTime;

    // Constructor to convert OpeningHours entity to OpeningHoursDTO
    public OpeningHoursDTO(OpeningHours openingHours) {
        if (openingHours != null) {
            this.id = openingHours.getId();
            this.weekday = openingHours.getWeekday();
            this.openTime = openingHours.getOpenTime();
            this.closeTime = openingHours.getCloseTime();
        }
    }

    // Convert DTO to Entity
    public OpeningHours toEntity() {
        OpeningHours openingHours = new OpeningHours();
        openingHours.setId(this.id);
        openingHours.setWeekday(this.weekday);
        openingHours.setOpenTime(this.openTime);
        openingHours.setCloseTime(this.closeTime);
        return openingHours;
    }
}