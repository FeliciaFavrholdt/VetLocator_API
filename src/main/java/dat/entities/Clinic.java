package dat.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "clinics")
public class Clinic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "address", nullable = false, length = 255)
    private String address;

    @Column(name = "contact_phone", nullable = false, length = 15)
    private String contactPhone;

    @ManyToOne
    @JoinColumn(name = "city_id", nullable = false)
    private City city;

    @Column(name = "emergency_services", nullable = false)
    private Boolean emergencyServices;
}