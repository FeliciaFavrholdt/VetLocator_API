package dat.dto;

import dat.entities.Clinic;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private Long cityId;  // Reference to the city

    // Constructor to convert from Clinic entity to ClinicDTO
    public ClinicDTO(Clinic clinic) {
        this.id = clinic.getId();
        this.name = clinic.getName();
        this.address = clinic.getAddress();
        this.contactPhone = clinic.getContactPhone();
        this.emergencyServices = clinic.getEmergencyServices();
        this.cityId = clinic.getCity() != null ? clinic.getCity().getId() : null;  // Fetch city ID
    }

    // Method to convert from DTO to Entity
    public Clinic toEntity() {
        Clinic clinic = new Clinic();
        clinic.setId(this.id);
        clinic.setName(this.name);
        clinic.setAddress(this.address);
        clinic.setContactPhone(this.contactPhone);
        clinic.setEmergencyServices(this.emergencyServices);
        // City should be set elsewhere (in the DAO or service layer) as it's an entity reference
        return clinic;
    }
}