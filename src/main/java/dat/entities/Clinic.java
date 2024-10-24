package dat.entities;

import dat.dto.ClinicDTO;
import dat.enums.Specialization;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "veterinary_clinic")
public class Clinic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;

    @Column(name = "clinic_name", nullable = false, length = 50)
    @NotBlank(message = "Clinic name cannot be blank")
    @Size(max = 50, message = "Clinic name must be 50 characters or less")
    private String clinicName;

    @Enumerated(EnumType.STRING)
    @Column(name = "specialization", nullable = false)
    private Specialization specialization;

    @Column(name = "phone", nullable = false, length = 50)
    @NotBlank(message = "Phone number cannot be blank")
    @Pattern(regexp = "\\+45 \\d{2} \\d{2} \\d{2} \\d{2}", message = "Phone number must be in the format +45 XX XX XX XX")
    private String phone;

    @Column(name = "email", nullable = false, length = 100)
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email cannot be blank")
    @Size(max = 100, message = "Email must be 100 characters or less")
    private String email;

    @Column(name = "address", nullable = false, length = 255)
    @NotBlank(message = "Address cannot be blank")
    @Size(max = 255, message = "Address must be 255 characters or less")
    private String address;

    @Column(name = "postal_code", nullable = false)
    private int postalCode;

    // OneToMany relationship with OpeningHours
    @OneToMany(mappedBy = "veterinaryClinic", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<OpeningHours> openingHours;

    // Add convenience method to populate opening hours
    public void addOpeningHour(OpeningHours openingHour) {
        if (openingHours == null) {
            openingHours = new HashSet<>();
        }
        openingHours.add(openingHour);
        openingHour.setVeterinaryClinic(this);
    }

    @OneToMany(mappedBy = "clinic", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Veterinarian> veterinarians;

    // Constructor to create a Clinic from ClinicDTO and City
    public Clinic(ClinicDTO dto, City city) {
        this.clinicName = dto.getClinicName();
        this.specialization = dto.getSpecialization();
        this.phone = dto.getPhone();
        this.email = dto.getEmail();
        this.address = dto.getAddress();
        this.postalCode = city.getPostalCode();  // Use the postal code from the City entity
    }

    // Additional constructor (in case you need it for other cases)
    public Clinic(String clinicName, Specialization specialization, String phone, String email, String address, int postalCode) {
        this.clinicName = clinicName;
        this.specialization = specialization;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.postalCode = postalCode;
    }

    // Method to update Clinic entity from ClinicDTO
    public void updateFromDTO(ClinicDTO dto, City city) {
        this.clinicName = dto.getClinicName();
        this.specialization = dto.getSpecialization();
        this.phone = dto.getPhone();
        this.email = dto.getEmail();
        this.address = dto.getAddress();
        // Postal code may come from a City update process
    }
}
