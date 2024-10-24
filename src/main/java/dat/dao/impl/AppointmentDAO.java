package dat.dao.impl;

import dat.dao.IDAO;
import dat.dto.AppointmentDTO;
import dat.entities.Animal;
import dat.entities.Appointment;
import dat.entities.Client;
import dat.entities.Clinic;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class AppointmentDAO implements IDAO<AppointmentDTO, Integer> {

    private static AppointmentDAO instance;
    private final EntityManagerFactory emf;

    private AppointmentDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public static AppointmentDAO getInstance(EntityManagerFactory emf) {
        if (instance == null) {
            instance = new AppointmentDAO(emf);
        }
        return instance;
    }

    @Override
    public AppointmentDTO create(AppointmentDTO dto) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Appointment appointment = new Appointment();
            mapDTOtoEntity(dto, appointment, em);  // Map DTO data to the entity
            em.persist(appointment);
            em.getTransaction().commit();
            return mapEntityToDTO(appointment);
        } finally {
            em.close();
        }
    }

    @Override
    public AppointmentDTO read(Integer id) {
        EntityManager em = emf.createEntityManager();
        try {
            Appointment appointment = em.find(Appointment.class, id);
            return appointment != null ? mapEntityToDTO(appointment) : null;
        } finally {
            em.close();
        }
    }

    @Override
    public List<AppointmentDTO> readAll() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Appointment> query = em.createQuery("SELECT a FROM Appointment a", Appointment.class);
            List<Appointment> appointments = query.getResultList();
            return appointments.stream().map(this::mapEntityToDTO).toList();
        } finally {
            em.close();
        }
    }

    @Override
    public AppointmentDTO update(Integer id, AppointmentDTO dto) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Appointment appointment = em.find(Appointment.class, id);
            if (appointment != null) {
                mapDTOtoEntity(dto, appointment, em);  // Update entity with new data from DTO
                em.merge(appointment);
                em.getTransaction().commit();
                return mapEntityToDTO(appointment);
            }
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(Integer id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Appointment appointment = em.find(Appointment.class, id);
            if (appointment != null) {
                em.remove(appointment);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Override
    public boolean validatePrimaryKey(Integer id) {
        return id != null && id > 0;
    }

    // Helper method to map AppointmentDTO to Appointment entity
    private void mapDTOtoEntity(AppointmentDTO dto, Appointment appointment, EntityManager em) {
        appointment.setDate(dto.getDate());
        appointment.setTime(dto.getTime());
        appointment.setReason(dto.getReason());
        appointment.setStatus(dto.getStatus());

        // Fetch the actual Clinic (Veterinary) entity by its ID
        if (dto.getVeterinarianId() != null) {
            Clinic clinic = em.find(Clinic.class, dto.getVeterinarianId());
            appointment.setVeterinaryClinic(clinic);
        }

        // Fetch the actual Client entity by its ID
        if (dto.getClientId() != null) {
            Client client = em.find(Client.class, dto.getClientId());
            appointment.setClient(client);
        }

        // Fetch the actual Animal entity by its ID
        if (dto.getAnimalId() != null) {
            Animal animal = em.find(Animal.class, dto.getAnimalId());
            appointment.setAnimal(animal);
        }
    }

    // Helper method to map Appointment entity to AppointmentDTO
    private AppointmentDTO mapEntityToDTO(Appointment appointment) {
        return AppointmentDTO.builder()
                .id(appointment.getId())
                .date(appointment.getDate())
                .time(appointment.getTime())
                .reason(appointment.getReason())
                .status(appointment.getStatus())
                // Map Clinic (Veterinary) entity to its ID and name
                .veterinarianId(appointment.getVeterinaryClinic() != null ? appointment.getVeterinaryClinic().getId() : null)
                .veterinarianName(appointment.getVeterinaryClinic() != null ? appointment.getVeterinaryClinic().getClinicName() : null)
                // Map Client entity to its ID and name
                .clientId(appointment.getClient() != null ? appointment.getClient().getId() : null)
                .clientName(appointment.getClient() != null ? appointment.getClient().getFirstName() + " " + appointment.getClient().getLastName() : null)
                // Map Animal entity to its ID and name
                .animalId(appointment.getAnimal() != null ? appointment.getAnimal().getId() : null)
                .animalName(appointment.getAnimal() != null ? appointment.getAnimal().getName() : null)
                .build();
    }
}
