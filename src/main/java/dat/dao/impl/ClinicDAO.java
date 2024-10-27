package dat.dao.impl;

import dat.dao.IDAO;
import dat.dto.ClinicDTO;
import dat.entities.Clinic;
import dat.entities.City;
import dat.exceptions.JpaException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceException;

import java.util.List;

public class ClinicDAO implements IDAO<ClinicDTO, Long> {

    private static ClinicDAO instance;
    private static EntityManagerFactory emf;

    private ClinicDAO() {}

    public static ClinicDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new ClinicDAO();
        }
        return instance;
    }

    @Override
    public ClinicDTO create(ClinicDTO clinicDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            // Find the city by ID (this assumes city is passed via DTO)
            City city = em.find(City.class, clinicDTO.getCityId());
            if (city == null) {
                throw new JpaException(400, "City not found for ID: " + clinicDTO.getCityId());
            }

            // Create a new clinic and populate its fields from the DTO
            Clinic clinic = new Clinic();
            clinic.convertFromDTO(clinicDTO, city);

            // Persist the clinic entity
            em.persist(clinic);
            em.getTransaction().commit();

            // Return the created entity as a DTO
            return new ClinicDTO(clinic);
        } catch (PersistenceException e) {
            throw new JpaException(500, "Error creating clinic in the database.");
        }
    }

    @Override
    public ClinicDTO read(Long id) {
        try (EntityManager em = emf.createEntityManager()) {
            Clinic clinic = em.find(Clinic.class, id);
            if (clinic == null) {
                throw new JpaException(404, "Clinic not found for ID: " + id);
            }
            return new ClinicDTO(clinic);
        } catch (PersistenceException e) {
            throw new JpaException(500, "Error reading clinic from the database.");
        }
    }

    @Override
    public List<ClinicDTO> readAll() {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery("SELECT new dat.dto.ClinicDTO(c) FROM Clinic c", ClinicDTO.class)
                    .getResultList();
        } catch (PersistenceException e) {
            throw new JpaException(500, "Error fetching clinics from the database.");
        }
    }

    @Override
    public ClinicDTO update(Long id, ClinicDTO clinicDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            // Find the existing clinic by ID
            Clinic clinic = em.find(Clinic.class, id);
            if (clinic == null) {
                throw new JpaException(404, "Clinic not found for ID: " + id);
            }

            // Find the city by ID (in case it's updated)
            City city = em.find(City.class, clinicDTO.getCityId());
            if (city == null) {
                throw new JpaException(400, "City not found for ID: " + clinicDTO.getCityId());
            }

            // Update the clinic with data from the DTO
            clinic.convertFromDTO(clinicDTO, city);

            // Merge and commit the changes
            Clinic updatedClinic = em.merge(clinic);
            em.getTransaction().commit();

            return new ClinicDTO(updatedClinic);
        } catch (PersistenceException e) {
            throw new JpaException(500, "Error updating clinic in the database.");
        }
    }

    @Override
    public void delete(Long id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Clinic clinic = em.find(Clinic.class, id);
            if (clinic == null) {
                throw new JpaException(404, "Clinic not found for ID: " + id);
            }
            em.remove(clinic);
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            throw new JpaException(500, "Error deleting clinic from the database.");
        }
    }

    @Override
    public boolean validatePrimaryKey(Long id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(Clinic.class, id) != null;
        } catch (PersistenceException e) {
            throw new JpaException(500, "Error validating clinic primary key.");
        }
    }
}