package dat.entities;

import dat.dto.ClinicDTO;
import dat.enums.Specialization;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

@Entity
@Data
@Builder
@ToString(exclude = "city")
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", nullable = false)
    private City city;

    @OneToMany(mappedBy = "clinic", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Veterinarian> veterinarians;

    public Clinic(String clinicName, Specialization specialization, String phone, String email, String address, City city) {
        this.clinicName = clinicName;
        this.specialization = specialization;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.city = city;
    }

    // Constructor to create a Clinic from ClinicDTO
    public Clinic(ClinicDTO dto, City city) {
        this.clinicName = dto.getClinicName();
        this.specialization = dto.getSpecialization();
        this.phone = dto.getPhone();
        this.email = dto.getEmail();
        this.address = dto.getAddress();
        this.city = city;  // Set city based on City entity
    }

    // Method to update Clinic entity from ClinicDTO
    public void updateFromDTO(ClinicDTO dto, City city) {
        this.clinicName = dto.getClinicName();
        this.specialization = dto.getSpecialization();
        this.phone = dto.getPhone();
        this.email = dto.getEmail();
        this.address = dto.getAddress();
        this.city = city;  // Update city
    }
}
