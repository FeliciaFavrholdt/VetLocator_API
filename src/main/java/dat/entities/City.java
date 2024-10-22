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
    private Long id;

    @Column(name = "city_name", nullable = false, length = 100)
    @NotBlank(message = "City name cannot be blank")
    @Size(max = 100, message = "City name must be less than or equal to 100 characters")
    private String cityName;

    @Column(name = "address", nullable = false, length = 255)
    @NotBlank(message = "Address cannot be blank")
    @Size(max = 255, message = "Address must be less than or equal to 255 characters")
    private String address;

    @Column(name = "postal_code", nullable = false, length = 10)
    @NotBlank(message = "Postal code cannot be blank")
    @Size(max = 10, message = "Postal code must be less than or equal to 10 characters")
    private String postalCode;
}
