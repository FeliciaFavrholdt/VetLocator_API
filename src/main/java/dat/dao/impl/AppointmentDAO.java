package dat.dao.impl;

import dat.dao.IDAO;
import dat.dto.AppointmentDTO;
import dat.entities.Appointment;
import dat.entities.Animal;
import dat.entities.Veterinarian;
import dat.entities.Clinic;
import dat.entities.Client;
import dat.exceptions.JpaException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.PersistenceException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class AppointmentDAO implements IDAO<AppointmentDTO, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(AppointmentDAO.class);  // Logger instance
    private static AppointmentDAO instance;
    private static EntityManagerFactory emf;

    public AppointmentDAO(EntityManagerFactory emf) {
        AppointmentDAO.emf = emf;
    }

    public static AppointmentDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new AppointmentDAO();
        }
        return instance;
    }

    @Override
    public AppointmentDTO create(AppointmentDTO appointmentDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            // Fetch related entities (Clinic, Client, Animal, Veterinarian)
            Animal animal = em.find(Animal.class, appointmentDTO.getAnimalId());
            Veterinarian veterinarian = em.find(Veterinarian.class, appointmentDTO.getVeterinarianId());
            Clinic clinic = em.find(Clinic.class, appointmentDTO.getClinicId());
            Client client = em.find(Client.class, appointmentDTO.getClientId());

            // Validation
            if (animal == null) {
                logger.warn("Animal not found for ID: {}", appointmentDTO.getAnimalId());
                throw new JpaException(400, "Animal not found");
            }
            if (veterinarian == null) {
                logger.warn("Veterinarian not found for ID: {}", appointmentDTO.getVeterinarianId());
                throw new JpaException(400, "Veterinarian not found");
            }
            if (clinic == null) {
                logger.warn("Clinic not found for ID: {}", appointmentDTO.getClinicId());
                throw new JpaException(400, "Clinic not found");
            }
            if (client == null) {
                logger.warn("Client not found for ID: {}", appointmentDTO.getClientId());
                throw new JpaException(400, "Client not found");
            }

            // Convert DTO to Entity and pass required entities
            Appointment appointment = appointmentDTO.toEntity(clinic, client, animal, veterinarian);

            em.persist(appointment);
            em.getTransaction().commit();
            logger.info("Appointment created successfully with ID {}", appointment.getId());
            return new AppointmentDTO(appointment);
        } catch (PersistenceException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            logger.error("Error creating appointment in the database: {}", e.getMessage());
            throw new JpaException(500, "Error creating appointment in the database.");
        } finally {
            em.close();
        }
    }

    @Override
    public AppointmentDTO read(Integer id) {
        EntityManager em = emf.createEntityManager();
        try {
            Appointment appointment = em.find(Appointment.class, id);
            if (appointment == null) {
                logger.warn("Appointment not found for ID: {}", id);
                throw new JpaException(404, "Appointment not found for ID: " + id);
            }
            logger.info("Appointment with ID {} successfully retrieved.", id);
            return new AppointmentDTO(appointment);
        } finally {
            em.close();
        }
    }

    @Override
    public List<AppointmentDTO> readAll() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<AppointmentDTO> query = em.createQuery("SELECT new dat.dto.AppointmentDTO(a) FROM Appointment a", AppointmentDTO.class);
            List<AppointmentDTO> appointments = query.getResultList();
            logger.info("Successfully retrieved {} appointments.", appointments.size());
            return appointments;
        } finally {
            em.close();
        }
    }

    @Override
    public AppointmentDTO update(Integer id, AppointmentDTO appointmentDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Appointment appointment = em.find(Appointment.class, id);
            if (appointment == null) {
                logger.warn("Appointment not found for ID: {}", id);
                throw new JpaException(404, "Appointment not found for ID: " + id);
            }

            // Fetch related entities (Clinic, Client, Animal, Veterinarian)
            Animal animal = em.find(Animal.class, appointmentDTO.getAnimalId());
            Veterinarian veterinarian = em.find(Veterinarian.class, appointmentDTO.getVeterinarianId());
            Clinic clinic = em.find(Clinic.class, appointmentDTO.getClinicId());
            Client client = em.find(Client.class, appointmentDTO.getClientId());

            // Validation
            if (animal == null) {
                logger.warn("Animal not found for ID: {}", appointmentDTO.getAnimalId());
                throw new JpaException(400, "Animal not found");
            }
            if (veterinarian == null) {
                logger.warn("Veterinarian not found for ID: {}", appointmentDTO.getVeterinarianId());
                throw new JpaException(400, "Veterinarian not found");
            }
            if (clinic == null) {
                logger.warn("Clinic not found for ID: {}", appointmentDTO.getClinicId());
                throw new JpaException(400, "Clinic not found");
            }
            if (client == null) {
                logger.warn("Client not found for ID: {}", appointmentDTO.getClientId());
                throw new JpaException(400, "Client not found");
            }

            // Update appointment fields from DTO
            appointment.convertFromDTO(appointmentDTO);
            appointment.setAnimal(animal);
            appointment.setVeterinarian(veterinarian);
            appointment.setClinic(clinic);
            appointment.setClient(client);

            Appointment mergedAppointment = em.merge(appointment);
            em.getTransaction().commit();
            logger.info("Appointment with ID {} successfully updated.", id);
            return new AppointmentDTO(mergedAppointment);
        } catch (PersistenceException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            logger.error("Error updating appointment in the database: {}", e.getMessage());
            throw new JpaException(500, "Error updating appointment in the database.");
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
            if (appointment == null) {
                logger.warn("Appointment not found for ID: {}", id);
                throw new JpaException(404, "Appointment not found for ID: " + id);
            }
            em.remove(appointment);
            em.getTransaction().commit();
            logger.info("Appointment with ID {} successfully deleted.", id);
        } catch (PersistenceException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            logger.error("Error deleting appointment from the database: {}", e.getMessage());
            throw new JpaException(500, "Error deleting appointment from the database.");
        } finally {
            em.close();
        }
    }

    @Override
    public boolean validatePrimaryKey(Integer id) {
        EntityManager em = emf.createEntityManager();
        try {
            boolean isValid = em.find(Appointment.class, id) != null;
            if (!isValid) {
                logger.warn("Invalid primary key: {}", id);
            }
            return isValid;
        } finally {
            em.close();
        }
    }
}