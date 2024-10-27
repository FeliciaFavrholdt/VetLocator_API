package dat.dto;

import dat.entities.Veterinarian;
import dat.enums.Specialization;
import dat.enums.Availability;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VeterinarianDTO {

    private Long id;
    private String name;
    private String specialties;
    private String availableForEmergency;
    private Long clinicId;

    // Constructor to convert from Veterinarian entity to VeterinarianDTO
    public VeterinarianDTO(Veterinarian veterinarian) {
        this.id = veterinarian.getId();
        this.name = veterinarian.getName();
        this.specialties = veterinarian.getSpecialties().toString(); // Assuming specialties is an enum
        this.availableForEmergency = veterinarian.getAvailableForEmergency().toString(); // Enum to String
        this.clinicId = veterinarian.getClinic() != null ? veterinarian.getClinic().getId() : null;
    }

    // Method to convert from DTO to Entity
    public Veterinarian toEntity() {
        Veterinarian veterinarian = new Veterinarian();
        veterinarian.setId(this.id);
        veterinarian.setName(this.name);
        veterinarian.setSpecialties(Specialization.valueOf(this.specialties));
        veterinarian.setAvailableForEmergency(Availability.valueOf(this.availableForEmergency));
        return veterinarian;
    }
}