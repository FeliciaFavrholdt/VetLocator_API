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

    private Long id;
    private String clinicName;
    private Specialization specialization;
    private String phone;
    private String email;
    private String address;       // Clinic's address
    private String cityName;      // City name from the City entity
    private String cityAddress;   // City address from the City entity
    private String postalCode;    // City postal code

    // Constructor to create ClinicDTO from Clinic entity
    public ClinicDTO(Clinic clinic) {
        this.id = clinic.getId();
        this.clinicName = clinic.getClinicName();
        this.specialization = clinic.getSpecialization();
        this.phone = clinic.getPhone();
        this.email = clinic.getEmail();
        this.address = clinic.getAddress();

        // Extracting city details from the City entity
        this.cityName = clinic.getCity().getCityName();
        this.cityAddress = clinic.getCity().getAddress();
        this.postalCode = clinic.getCity().getPostalCode();
    }
}