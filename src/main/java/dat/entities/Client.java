package dat.entities;

import dat.dto.ClientDTO;
import dat.enums.Gender;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false, length = 10)
    private Gender gender;

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

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Animal> animals = new ArrayList<>();

    // New constructor that takes a list of animals
    public Client(Long id, String name, Gender gender, String email, String phoneNumber, String address, City city, List<Animal> animals) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.city = city;
        this.animals = new ArrayList<>();  // Initialize the list
        // Set the bidirectional relationship
        for (Animal animal : animals) {
            this.addAnimal(animal);  // Use addAnimal to maintain the relationship
        }
    }

    // Method to add an animal and maintain the relationship
    public void addAnimal(Animal animal) {
        animals.add(animal);
        animal.setOwner(this);  // Maintain the bidirectional relationship
    }

    // Method to remove an animal and maintain the relationship
    public void removeAnimal(Animal animal) {
        animals.remove(animal);
        animal.setOwner(null);  // Break the relationship
    }
}