package dat.dto;

import dat.entities.Client;
import dat.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientDTO {

    private Long id;
    private String name;
    private String gender;  // Store gender as a string in DTO
    private String email;
    private String phoneNumber;
    private String address;
    private Long cityId;  // Reference to the city

    // Constructor to convert from Client entity to ClientDTO
    public ClientDTO(Client client) {
        this.id = client.getId();
        this.name = client.getName();
        this.gender = client.getGender() != null ? client.getGender().toString() : null;  // Convert Gender enum to String
        this.email = client.getEmail();
        this.phoneNumber = client.getPhoneNumber();
        this.address = client.getAddress();
        this.cityId = client.getCity() != null ? client.getCity().getId() : null;  // Fetch city ID if available
    }

    // Method to convert from DTO to Entity
    public Client toEntity() {
        Client client = new Client();
        client.setId(this.id);
        client.setName(this.name);
        client.setGender(this.gender != null ? Gender.valueOf(this.gender) : null);  // Convert String back to Gender enum
        client.setEmail(this.email);
        client.setPhoneNumber(this.phoneNumber);
        client.setAddress(this.address);
        // City should be set elsewhere (in the DAO or service layer)
        return client;
    }
}