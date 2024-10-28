package dat.entities;

import dat.dto.ClientDTO;
import dat.enums.Gender;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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
    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+45\\s\\d{2}\\s\\d{2}\\s\\d{2}\\s\\d{2}$", message = "Phone number must match +45 xx xx xx xx format")
    @Column(name = "phone_number", nullable = false, length = 15)
    private String phoneNumber;

    @NotBlank(message = "Address is required")
    @Size(max = 255, message = "Address must not exceed 255 characters")
    @Column(name = "address", nullable = false, length = 255)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false, length = 6)
    private Gender gender;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Animal> animals = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)  // Optional: Lazy loading
    @JoinColumn(name = "city_id", nullable = false)
    private City city;  // Many Clients belong to one City

    // Constructor without validation annotations (to use in DTOs, etc.)
    public Client(Long id, String name, Gender gender, String email, String phoneNumber, String address, City city, List<Animal> animals) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.city = city;
        this.animals = animals != null ? animals : new ArrayList<>();
    }

    // Method to convert from ClientDTO to Client
    public void convertFromDTO(ClientDTO clientDTO) {
        this.name = clientDTO.getName();
        this.email = clientDTO.getEmail();
        this.phoneNumber = clientDTO.getPhoneNumber();
        this.address = clientDTO.getAddress();
        this.city = city;  // Set city during conversion

        // Handle gender conversion
        if (clientDTO.getGender() != null) {
            try {
                this.gender = Gender.valueOf(clientDTO.getGender().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid gender value: " + clientDTO.getGender());
            }
        } else {
            this.gender = null;  // If gender is null, set it to null
        }
    }
}
