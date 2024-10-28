package dat.dto;

import dat.entities.Clinic;
import dat.entities.OpeningHours;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClinicDTO {

    private Long id;
    private String name;
    private String address;
    private String contactPhone;
    private Boolean emergencyServices;
    private Long cityId;
    private List<OpeningHoursDTO> openingHours;  // List of opening hours

    // Constructor to convert from Clinic entity to ClinicDTO
    public ClinicDTO(Clinic clinic) {
        if (clinic != null) {
            this.id = clinic.getId();
            this.name = clinic.getName();
            this.address = clinic.getAddress();
            this.contactPhone = clinic.getContactPhone();
            this.emergencyServices = clinic.getEmergencyServices();
            // Convert City to cityId (if city exists)
            this.cityId = clinic.getCity() != null ? clinic.getCity().getId() : null;

            // Convert OpeningHours entity list to OpeningHoursDTO list
            this.openingHours = clinic.getOpeningHours() != null ?
                    clinic.getOpeningHours().stream().map(OpeningHoursDTO::new).collect(Collectors.toList()) :
                    null;
        }
    }

    // Method to convert from DTO to Clinic entity (excluding City)
    public Clinic toEntity() {
        Clinic clinic = new Clinic();
        clinic.setId(this.id);
        clinic.setName(this.name);
        clinic.setAddress(this.address);
        clinic.setContactPhone(this.contactPhone);
        clinic.setEmergencyServices(this.emergencyServices);
        // City association should be handled separately in the service/DAO layer
        // OpeningHours should also be set in the service/DAO layer
        return clinic;
    }
}