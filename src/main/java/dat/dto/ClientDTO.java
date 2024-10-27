package dat.dto;

import dat.entities.Client;
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
    private String email;
    private String phoneNumber;
    private String address;
    private Long cityId;

    // Constructor to convert from Client entity to ClientDTO
    public ClientDTO(Client client) {
        this.id = client.getId();
        this.name = client.getName();
        this.email = client.getEmail();
        this.phoneNumber = client.getPhoneNumber();
        this.address = client.getAddress();
        this.cityId = client.getCity() != null ? client.getCity().getId() : null;
    }

    // Method to convert from DTO to Entity
    public Client toEntity() {
        Client client = new Client();
        client.setId(this.id);
        client.setName(this.name);
        client.setEmail(this.email);
        client.setPhoneNumber(this.phoneNumber);
        client.setAddress(this.address);
        return client;
    }
}
