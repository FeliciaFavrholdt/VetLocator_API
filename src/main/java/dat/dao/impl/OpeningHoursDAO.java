package dat.dao.impl;

import dat.dao.IDAO;
import dat.dto.OpeningHoursDTO;
import dat.entities.Clinic;
import dat.entities.OpeningHours;
import dat.exceptions.JpaException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.PersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class OpeningHoursDAO implements IDAO<OpeningHoursDTO, Long> {

    private static final Logger logger = LoggerFactory.getLogger(OpeningHoursDAO.class);

    private static OpeningHoursDAO instance;
    private final EntityManagerFactory emf;

    private OpeningHoursDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public static OpeningHoursDAO getInstance(EntityManagerFactory emf) {
        if (instance == null) {
            instance = new OpeningHoursDAO(emf);
        }
        return instance;
    }

    @Override
    public OpeningHoursDTO create(OpeningHoursDTO dto) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            OpeningHours openingHours = new OpeningHours();
            mapDTOtoEntity(dto, openingHours, em);
            em.persist(openingHours);
            em.getTransaction().commit();
            logger.info("Opening hours created successfully with ID: {}", openingHours.getId());
            return mapEntityToDTO(openingHours);
        } catch (PersistenceException e) {
            em.getTransaction().rollback();
            logger.error("Error creating opening hours: {}", e.getMessage(), e);
            throw new JpaException(500, "Error creating opening hours in the database.");
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public OpeningHoursDTO read(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            OpeningHours openingHours = em.find(OpeningHours.class, id);
            if (openingHours == null) {
                throw new JpaException(404, "Opening hours not found for ID: " + id);
            }
            return mapEntityToDTO(openingHours);
        } catch (PersistenceException e) {
            logger.error("Error reading opening hours with ID {}: {}", id, e.getMessage(), e);
            throw new JpaException(500, "Error reading opening hours from the database.");
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public List<OpeningHoursDTO> readAll() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<OpeningHours> query = em.createQuery("SELECT o FROM OpeningHours o", OpeningHours.class);
            List<OpeningHours> openingHoursList = query.getResultList();
            logger.info("Fetched {} opening hours from the database.", openingHoursList.size());
            return openingHoursList.stream().map(this::mapEntityToDTO).toList();
        } catch (PersistenceException e) {
            logger.error("Error fetching all opening hours: {}", e.getMessage(), e);
            throw new JpaException(500, "Error fetching opening hours from the database.");
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public OpeningHoursDTO update(Long id, OpeningHoursDTO dto) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            OpeningHours openingHours = em.find(OpeningHours.class, id);
            if (openingHours == null) {
                throw new JpaException(404, "Opening hours not found for ID: " + id);
            }
            mapDTOtoEntity(dto, openingHours, em);
            em.merge(openingHours);
            em.getTransaction().commit();
            logger.info("Opening hours updated successfully with ID: {}", openingHours.getId());
            return mapEntityToDTO(openingHours);
        } catch (PersistenceException e) {
            em.getTransaction().rollback();
            logger.error("Error updating opening hours with ID {}: {}", id, e.getMessage(), e);
            throw new JpaException(500, "Error updating opening hours in the database.");
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public void delete(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            OpeningHours openingHours = em.find(OpeningHours.class, id);
            if (openingHours == null) {
                throw new JpaException(404, "Opening hours not found for ID: " + id);
            }
            em.remove(openingHours);
            em.getTransaction().commit();
            logger.info("Opening hours deleted successfully with ID: {}", id);
        } catch (PersistenceException e) {
            em.getTransaction().rollback();
            logger.error("Error deleting opening hours with ID {}: {}", id, e.getMessage(), e);
            throw new JpaException(500, "Error deleting opening hours from the database.");
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public boolean validatePrimaryKey(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            OpeningHours openingHours = em.find(OpeningHours.class, id);
            return openingHours != null;
        } catch (PersistenceException e) {
            logger.error("Error validating opening hours primary key with ID {}: {}", id, e.getMessage(), e);
            throw new JpaException(500, "Error validating opening hours primary key.");
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }

    // Helper method to map OpeningHoursDTO to OpeningHours entity
    private void mapDTOtoEntity(OpeningHoursDTO dto, OpeningHours openingHours, EntityManager em) {
        openingHours.setWeekday(dto.getWeekday());
        openingHours.setStartTime(dto.getStartTime());
        openingHours.setEndTime(dto.getEndTime());

        if (dto.getVeterinaryClinicId() != null) {
            Clinic clinic = em.find(Clinic.class, dto.getVeterinaryClinicId());
            if (clinic != null) {
                openingHours.setVeterinaryClinic(clinic);
            } else {
                throw new JpaException(404, "Clinic not found for ID: " + dto.getVeterinaryClinicId());
            }
        }
    }

    // Helper method to map OpeningHours entity to OpeningHoursDTO
    private OpeningHoursDTO mapEntityToDTO(OpeningHours openingHours) {
        return OpeningHoursDTO.builder()
                .id(openingHours.getId())
                .weekday(openingHours.getWeekday())
                .startTime(openingHours.getStartTime())
                .endTime(openingHours.getEndTime())
                .veterinaryClinicId(Long.valueOf(openingHours.getVeterinaryClinic() != null ? openingHours.getVeterinaryClinic().getId() : null))
                .build();
    }
}