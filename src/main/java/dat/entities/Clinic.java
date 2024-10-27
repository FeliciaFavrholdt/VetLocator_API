package dat.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "clinics")
public class Clinic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "address", nullable = false, length = 255)
    private String address;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+45\\s\\d{2}\\s\\d{2}\\s\\d{2}\\s\\d{2}$", message = "Phone number must match +45 xx xx xx xx format")
    @Column(name = "contact_phone", nullable = false, length = 15)
    private String contactPhone;

    @ManyToOne
    @JoinColumn(name = "city_id", nullable = false)
    private City city;

    @Column(name = "emergency_services", nullable = false)
    private Boolean emergencyServices;

    // One-to-many relationship with OpeningHours
    @OneToMany(mappedBy = "clinic", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OpeningHours> openingHours = new ArrayList<>();

    // Helper method to add an opening hour
    public void addOpeningHour(OpeningHours openingHour) {
        openingHours.add(openingHour);
        openingHour.setClinic(this);
    }

    // Helper method to remove an opening hour
    public void removeOpeningHour(OpeningHours openingHour) {
        openingHours.remove(openingHour);
        openingHour.setClinic(null);
    }
}