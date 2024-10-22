package dat.entities;

import dat.enums.Specialization;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class VeterinaryClinic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private Specialization specialization;

    private String phone;

    private String email;

    private String address;

    private String city;

    private int zipCode;
}