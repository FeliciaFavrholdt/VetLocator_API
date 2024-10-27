package dat.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "clinics")
public class Clinic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;
    private String contactPhone;

    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;

    private Boolean emergencyServices;
}

