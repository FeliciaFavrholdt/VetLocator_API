package dat.dao.impl;

import dat.dao.IDAO;
import dat.dto.ClinicDTO;
import dat.entities.Clinic;
import dat.entities.City;
import dat.exceptions.JpaException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ClinicDAO implements IDAO<ClinicDTO, Long> {

    private static final Logger logger = LoggerFactory.getLogger(ClinicDAO.class);  // Logger instance
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
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            // Find the city by ID (this assumes city is passed via DTO)
            City city = em.find(City.class, clinicDTO.getCityId());
            if (city == null) {
                logger.warn("City not found for ID: {}", clinicDTO.getCityId());
                throw new JpaException(400, "City not found for ID: " + clinicDTO.getCityId());
            }

            // Create a new clinic and populate its fields from the DTO
            Clinic clinic = new Clinic();
            clinic.convertFromDTO(clinicDTO, city);

            // Persist the clinic entity
            em.persist(clinic);
            em.getTransaction().commit();

            logger.info("Clinic created successfully with ID {}", clinic.getId());
            return new ClinicDTO(clinic);
        } catch (PersistenceException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            logger.error("Error creating clinic in the database: {}", e.getMessage());
            throw new JpaException(500, "Error creating clinic in the database.");
        } finally {
            em.close();
        }
    }

    @Override
    public ClinicDTO read(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            Clinic clinic = em.find(Clinic.class, id);
            if (clinic == null) {
                logger.warn("Clinic not found for ID: {}", id);
                throw new JpaException(404, "Clinic not found for ID: " + id);
            }
            logger.info("Clinic with ID {} successfully retrieved.", id);
            return new ClinicDTO(clinic);
        } catch (PersistenceException e) {
            logger.error("Error reading clinic from the database: {}", e.getMessage());
            throw new JpaException(500, "Error reading clinic from the database.");
        } finally {
            em.close();
        }
    }

    @Override
    public List<ClinicDTO> readAll() {
        EntityManager em = emf.createEntityManager();
        try {
            List<ClinicDTO> clinics = em.createQuery("SELECT new dat.dto.ClinicDTO(c) FROM Clinic c", ClinicDTO.class)
                    .getResultList();
            logger.info("Successfully retrieved {} clinics.", clinics.size());
            return clinics;
        } catch (PersistenceException e) {
            logger.error("Error fetching clinics from the database: {}", e.getMessage());
            throw new JpaException(500, "Error fetching clinics from the database.");
        } finally {
            em.close();
        }
    }

    @Override
    public ClinicDTO update(Long id, ClinicDTO clinicDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            // Find the existing clinic by ID
            Clinic clinic = em.find(Clinic.class, id);
            if (clinic == null) {
                logger.warn("Clinic not found for ID: {}", id);
                throw new JpaException(404, "Clinic not found for ID: " + id);
            }

            // Find the city by ID (in case it's updated)
            City city = em.find(City.class, clinicDTO.getCityId());
            if (city == null) {
                logger.warn("City not found for ID: {}", clinicDTO.getCityId());
                throw new JpaException(400, "City not found for ID: " + clinicDTO.getCityId());
            }

            // Update the clinic with data from the DTO
            clinic.convertFromDTO(clinicDTO, city);

            // Merge and commit the changes
            Clinic updatedClinic = em.merge(clinic);
            em.getTransaction().commit();

            logger.info("Clinic with ID {} successfully updated.", id);
            return new ClinicDTO(updatedClinic);
        } catch (PersistenceException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            logger.error("Error updating clinic in the database: {}", e.getMessage());
            throw new JpaException(500, "Error updating clinic in the database.");
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Clinic clinic = em.find(Clinic.class, id);
            if (clinic == null) {
                logger.warn("Clinic not found for ID: {}", id);
                throw new JpaException(404, "Clinic not found for ID: " + id);
            }
            em.remove(clinic);
            em.getTransaction().commit();
            logger.info("Clinic with ID {} successfully deleted.", id);
        } catch (PersistenceException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            logger.error("Error deleting clinic from the database: {}", e.getMessage());
            throw new JpaException(500, "Error deleting clinic from the database.");
        } finally {
            em.close();
        }
    }

    @Override
    public boolean validatePrimaryKey(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            boolean isValid = em.find(Clinic.class, id) != null;
            if (!isValid) {
                logger.warn("Invalid primary key: {}", id);
            }
            return isValid;
        } catch (PersistenceException e) {
            logger.error("Error validating clinic primary key: {}", e.getMessage());
            throw new JpaException(500, "Error validating clinic primary key.");
        } finally {
            em.close();
        }
    }
}