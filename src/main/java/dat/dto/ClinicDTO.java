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

    // Constructor to convert from Clinic entity to ClinicDTO
    public ClinicDTO(Clinic clinic) {
        this.id = clinic.getId();
        this.name = clinic.getName();
        this.address = clinic.getAddress();
        this.contactPhone = clinic.getContactPhone();
        this.emergencyServices = clinic.getEmergencyServices();
    }

    // Method to convert from DTO to Entity
    public Clinic toEntity() {
        Clinic clinic = new Clinic();
        clinic.setId(this.id);
        clinic.setName(this.name);
        clinic.setAddress(this.address);
        clinic.setContactPhone(this.contactPhone);
        clinic.setEmergencyServices(this.emergencyServices);
        return clinic;
    }
}