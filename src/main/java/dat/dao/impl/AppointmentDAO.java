package dat.dao.impl;

import dat.dao.IDAO;
import dat.dto.AppointmentDTO;
import dat.entities.Animal;
import dat.entities.Appointment;
import dat.entities.Client;
import dat.entities.Clinic;
import dat.exceptions.JpaException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.PersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class AppointmentDAO implements IDAO<AppointmentDTO, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(AppointmentDAO.class);

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
            mapDTOtoEntity(dto, appointment, em);
            em.persist(appointment);
            em.getTransaction().commit();
            logger.info("Appointment created successfully with ID: {}", appointment.getId());
            return mapEntityToDTO(appointment);
        } catch (PersistenceException e) {
            em.getTransaction().rollback();
            logger.error("Error creating appointment: {}", e.getMessage(), e);
            throw new JpaException(500, "Error creating appointment in the database.");
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public AppointmentDTO read(Integer id) {
        EntityManager em = emf.createEntityManager();
        try {
            Appointment appointment = em.find(Appointment.class, id);
            if (appointment == null) {
                throw new JpaException(404, "Appointment not found for ID: " + id);
            }
            return mapEntityToDTO(appointment);
        } catch (PersistenceException e) {
            logger.error("Error reading appointment with ID {}: {}", id, e.getMessage(), e);
            throw new JpaException(500, "Error reading appointment from the database.");
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public List<AppointmentDTO> readAll() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Appointment> query = em.createQuery("SELECT a FROM Appointment a", Appointment.class);
            List<Appointment> appointments = query.getResultList();
            logger.info("Fetched {} appointments from the database.", appointments.size());
            return appointments.stream().map(this::mapEntityToDTO).toList();
        } catch (PersistenceException e) {
            logger.error("Error fetching all appointments: {}", e.getMessage(), e);
            throw new JpaException(500, "Error fetching appointments from the database.");
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public AppointmentDTO update(Integer id, AppointmentDTO dto) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Appointment appointment = em.find(Appointment.class, id);
            if (appointment == null) {
                throw new JpaException(404, "Appointment not found for ID: " + id);
            }
            mapDTOtoEntity(dto, appointment, em);
            em.merge(appointment);
            em.getTransaction().commit();
            logger.info("Appointment updated successfully with ID: {}", appointment.getId());
            return mapEntityToDTO(appointment);
        } catch (PersistenceException e) {
            em.getTransaction().rollback();
            logger.error("Error updating appointment with ID {}: {}", id, e.getMessage(), e);
            throw new JpaException(500, "Error updating appointment in the database.");
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public void delete(Integer id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Appointment appointment = em.find(Appointment.class, id);
            if (appointment == null) {
                throw new JpaException(404, "Appointment not found for ID: " + id);
            }
            em.remove(appointment);
            em.getTransaction().commit();
            logger.info("Appointment deleted successfully with ID: {}", id);
        } catch (PersistenceException e) {
            em.getTransaction().rollback();
            logger.error("Error deleting appointment with ID {}: {}", id, e.getMessage(), e);
            throw new JpaException(500, "Error deleting appointment from the database.");
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public boolean validatePrimaryKey(Integer id) {
        EntityManager em = emf.createEntityManager();
        try {
            Appointment appointment = em.find(Appointment.class, id);
            return appointment != null;
        } catch (PersistenceException e) {
            logger.error("Error validating appointment primary key with ID {}: {}", id, e.getMessage(), e);
            throw new JpaException(500, "Error validating appointment primary key.");
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }

    // Helper method to map AppointmentDTO to Appointment entity
    private void mapDTOtoEntity(AppointmentDTO dto, Appointment appointment, EntityManager em) {
        appointment.setDate(dto.getDate());
        appointment.setTime(dto.getTime());
        appointment.setReason(dto.getReason());
        appointment.setStatus(dto.getStatus());

        if (dto.getVeterinarianId() != null) {
            Clinic clinic = em.find(Clinic.class, dto.getVeterinarianId());
            appointment.setVeterinaryClinic(clinic);
        }

        if (dto.getClientId() != null) {
            Client client = em.find(Client.class, dto.getClientId());
            appointment.setClient(client);
        }

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
                .veterinarianId(appointment.getVeterinaryClinic() != null ? appointment.getVeterinaryClinic().getId() : null)
                .veterinarianName(appointment.getVeterinaryClinic() != null ? appointment.getVeterinaryClinic().getClinicName() : null)
                .clientId(appointment.getClient() != null ? appointment.getClient().getId() : null)
                .clientName(appointment.getClient() != null ? appointment.getClient().getFirstName() + " " + appointment.getClient().getLastName() : null)
                .animalId(appointment.getAnimal() != null ? appointment.getAnimal().getId() : null)
                .animalName(appointment.getAnimal() != null ? appointment.getAnimal().getName() : null)
                .build();
    }
}