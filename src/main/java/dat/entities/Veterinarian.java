package dat.entities;

import dat.dto.VeterinarianDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import dat.enums.Specialization;
import dat.enums.Availability;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "veterinarians")
public class Veterinarian {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @NotNull(message = "Specialization is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "specialties", nullable = false, length = 50)
    private Specialization specialties;

    @NotNull(message = "Clinic is required")
    @ManyToOne
    @JoinColumn(name = "clinic_id", nullable = false)
    private Clinic clinic;

    @NotNull(message = "Availability for emergency is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "available_for_emergency", nullable = false, length = 10)
    private Availability availableForEmergency;

    public void convertFromDTO(VeterinarianDTO veterinarianDTO) {
        this.name = veterinarianDTO.getName();
        this.specialties = Specialization.valueOf(veterinarianDTO.getSpecialties());
        this.availableForEmergency = Availability.valueOf(veterinarianDTO.getAvailableForEmergency());
    }
}