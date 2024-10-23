package dat.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cities", indexes = @Index(name = "idx_postal_code", columnList = "postal_code"))  // Index for postal code
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;

    @Column(name = "city_name", nullable = false, length = 100)
    @NotBlank(message = "City name cannot be blank")
    @Size(max = 100, message = "City name must be less than or equal to 100 characters")
    private String cityName;

    @Column(name = "postal_code", nullable = false)
    private int postalCode;
}