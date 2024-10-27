package dat.entities;

import dat.dto.ClientDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+45\\s\\d{2}\\s\\d{2}\\s\\d{2}\\s\\d{2}$", message = "Phone number must match +45 xx xx xx xx format")
    @Column(name = "phone_number", nullable = false, length = 15)
    private String phoneNumber;

    @Size(max = 255, message = "Address must not exceed 255 characters")
    @Column(name = "address", length = 255)
    private String address;

    @NotNull(message = "City is required")
    @ManyToOne
    @JoinColumn(name = "city_id", nullable = false)
    private City city;

    // Method to convert from DTO
    public void convertFromDTO(ClientDTO clientDTO) {
        this.name = clientDTO.getName();
        this.email = clientDTO.getEmail();
        this.phoneNumber = clientDTO.getPhoneNumber();
        this.address = clientDTO.getAddress();
    }
}