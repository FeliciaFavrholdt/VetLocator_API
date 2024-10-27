package dat.dao.impl;

import dat.dao.IDAO;
import dat.dto.AppointmentDTO;
import dat.entities.Appointment;
import dat.entities.Animal;
import dat.entities.Veterinarian;
import dat.entities.Clinic;
import dat.exceptions.JpaException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.PersistenceException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class AppointmentDAO implements IDAO<AppointmentDTO, Integer> {

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
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Appointment appointment = appointmentDTO.toEntity();

            Animal animal = em.find(Animal.class, appointmentDTO.getAnimalId());
            Veterinarian veterinarian = em.find(Veterinarian.class, appointmentDTO.getVeterinarianId());
            Clinic clinic = em.find(Clinic.class, appointmentDTO.getClinicId());

            if (animal == null) throw new JpaException(400, "Animal not found");
            if (veterinarian == null) throw new JpaException(400, "Veterinarian not found");
            if (clinic == null) throw new JpaException(400, "Clinic not found");

            appointment.setAnimal(animal);
            appointment.setVeterinarian(veterinarian);
            appointment.setClinic(clinic);

            em.persist(appointment);
            em.getTransaction().commit();
            return new AppointmentDTO(appointment);
        } catch (PersistenceException e) {
            throw new JpaException(500, "Error creating appointment in the database.");
        }
    }

    @Override
    public AppointmentDTO read(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            Appointment appointment = em.find(Appointment.class, id);
            if (appointment == null) {
                throw new JpaException(404, "Appointment not found for ID: " + id);
            }
            return new AppointmentDTO(appointment);
        }
    }

    @Override
    public List<AppointmentDTO> readAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<AppointmentDTO> query = em.createQuery("SELECT new dat.dto.AppointmentDTO(a) FROM Appointment a", AppointmentDTO.class);
            return query.getResultList();
        }
    }

    @Override
    public AppointmentDTO update(Integer id, AppointmentDTO appointmentDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Appointment appointment = em.find(Appointment.class, id);
            if (appointment == null) {
                throw new JpaException(404, "Appointment not found for ID: " + id);
            }
            appointment.updateFromDTO(appointmentDTO);
            Animal animal = em.find(Animal.class, appointmentDTO.getAnimalId());
            Veterinarian veterinarian = em.find(Veterinarian.class, appointmentDTO.getVeterinarianId());
            Clinic clinic = em.find(Clinic.class, appointmentDTO.getClinicId());

            if (animal == null) throw new JpaException(400, "Animal not found");
            if (veterinarian == null) throw new JpaException(400, "Veterinarian not found");
            if (clinic == null) throw new JpaException(400, "Clinic not found");

            appointment.setAnimal(animal);
            appointment.setVeterinarian(veterinarian);
            appointment.setClinic(clinic);

            Appointment mergedAppointment = em.merge(appointment);
            em.getTransaction().commit();
            return new AppointmentDTO(mergedAppointment);
        } catch (PersistenceException e) {
            throw new JpaException(500, "Error updating appointment in the database.");
        }
    }

    @Override
    public void delete(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Appointment appointment = em.find(Appointment.class, id);
            if (appointment == null) {
                throw new JpaException(404, "Appointment not found for ID: " + id);
            }
            em.remove(appointment);
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            throw new JpaException(500, "Error deleting appointment from the database.");
        }
    }

    @Override
    public boolean validatePrimaryKey(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(Appointment.class, id) != null;
        }
    }
}