package dat.dto;

import dat.entities.Clinic;
import dat.enums.Specialization;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ClinicDTO {

    private Integer id;
    private String clinicName;
    private Specialization specialization;
    private String phone;
    private String email;
    private String address;       // Clinic's address
    private String cityName;      // City name
    private int postalCode;       // City postal code

    // Constructor to create ClinicDTO from Clinic entity
    public ClinicDTO(Clinic clinic) {
        this.id = clinic.getId();
        this.clinicName = clinic.getClinicName();
        this.specialization = clinic.getSpecialization();
        this.phone = clinic.getPhone();
        this.email = clinic.getEmail();
        this.address = clinic.getAddress();
        this.postalCode = clinic.getPostalCode();
    }
}
