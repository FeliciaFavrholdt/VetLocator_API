package dat.dao.impl;

import dat.dao.IDAO;
import dat.dto.ClinicDTO;
import dat.entities.Clinic;
import dat.entities.City;
import dat.enums.Weekday;
import dat.exceptions.ApiException;
import dat.exceptions.JpaException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.PersistenceException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class ClinicDAO implements IDAO<ClinicDTO, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(ClinicDAO.class);

    private static ClinicDAO instance;
    private static EntityManagerFactory emf;

    public static ClinicDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new ClinicDAO();
        }
        return instance;
    }

    public List<ClinicDTO> onDutyClinics() throws ApiException {

        // Find den aktuelle ugedag og tid
        Weekday currentWeekday = Weekday.valueOf(LocalDate.now().getDayOfWeek().name().toUpperCase());
        LocalTime currentTime = LocalTime.now();  // Definerer currentTime korrekt her

        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<ClinicDTO> query = em.createQuery("SELECT new dat.dto.ClinicDTO(c) " +
                    "FROM Clinic c " +
                    "JOIN c.openingHours oh " +
                    "WHERE oh.weekday = :currentWeekday " +
                    "AND oh.startTime <= :currentTime " +
                    "AND oh.endTime >= :currentTime", ClinicDTO.class);

            // Sæt parametrene for ugedag og tid
            query.setParameter("currentWeekday", currentWeekday);
            query.setParameter("currentTime", currentTime);

            // Udfør query og returner resultatet
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiException(500, "Error fetching clinics on duty");
        }

    }

    @Override
    public ClinicDTO create(ClinicDTO clinicDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            // Find City based on cityName and postalCode from ClinicDTO
            TypedQuery<City> cityQuery = em.createQuery("SELECT c FROM City c WHERE c.cityName = :cityName AND c.postalCode = :postalCode", City.class);
            cityQuery.setParameter("cityName", clinicDTO.getCityName());
            cityQuery.setParameter("postalCode", clinicDTO.getPostalCode());
            City city = cityQuery.getSingleResult();

            if (city == null) {
                throw new JpaException(400, "City not found for provided cityName and postalCode.");
            }

            // Convert DTO to Clinic entity and set the city
            Clinic clinic = new Clinic(clinicDTO, city);
            em.persist(clinic);
            em.getTransaction().commit();

            // Return the persisted Clinic entity as a DTO
            return new ClinicDTO(clinic);
        } catch (PersistenceException e) {
            em.getTransaction().rollback();
            logger.error("Error creating clinic: {}", e.getMessage(), e);
            throw new JpaException(500, "Error creating clinic in the database.");
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public ClinicDTO read(Integer id) {
        EntityManager em = emf.createEntityManager();
        try {
            Clinic clinic = em.find(Clinic.class, id);
            if (clinic == null) {
                throw new JpaException(404, "Clinic not found for ID: " + id);
            }
            return new ClinicDTO(clinic);
        } catch (PersistenceException e) {
            logger.error("Error reading clinic with ID {}: {}", id, e.getMessage(), e);
            throw new JpaException(500, "Error reading clinic from the database.");
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public List<ClinicDTO> readAll() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<ClinicDTO> query = em.createQuery("SELECT new dat.dto.ClinicDTO(c) FROM Clinic c", ClinicDTO.class);
            return query.getResultList();
        } catch (PersistenceException e) {
            logger.error("Error fetching all clinics: {}", e.getMessage(), e);
            throw new JpaException(500, "Error fetching clinics from the database.");
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public ClinicDTO update(Integer id, ClinicDTO clinicDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            // Find the existing Clinic by ID
            Clinic clinic = em.find(Clinic.class, id);
            if (clinic == null) {
                throw new JpaException(404, "Clinic not found for ID: " + id);
            }

            // Update fields only if they are provided (not null)
            if (clinicDTO.getClinicName() != null) {
                clinic.setClinicName(clinicDTO.getClinicName());
            }
            if (clinicDTO.getAddress() != null) {
                clinic.setAddress(clinicDTO.getAddress());
            }
            if (clinicDTO.getEmail() != null) {
                clinic.setEmail(clinicDTO.getEmail());
            }
            if (clinicDTO.getPhone() != null) {
                clinic.setPhone(clinicDTO.getPhone());
            }
            if (clinicDTO.getSpecialization() != null) {
                clinic.setSpecialization(clinicDTO.getSpecialization());
            }
            if (clinicDTO.getPostalCode() != 0000) {
                clinic.setPostalCode(clinicDTO.getPostalCode());
            }

            // Merge and commit the changes
            Clinic mergedClinic = em.merge(clinic);
            em.getTransaction().commit();

            return new ClinicDTO(mergedClinic);
        } catch (PersistenceException e) {
            em.getTransaction().rollback();
            logger.error("Error updating clinic with ID {}: {}", id, e.getMessage(), e);
            throw new JpaException(500, "Error updating clinic in the database.");
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
            Clinic clinic = em.find(Clinic.class, id);
            if (clinic == null) {
                throw new JpaException(404, "Clinic not found for ID: " + id);
            }
            em.remove(clinic);
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            em.getTransaction().rollback();
            logger.error("Error deleting clinic with ID {}: {}", id, e.getMessage(), e);
            throw new JpaException(500, "Error deleting clinic from the database.");
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
            Clinic clinic = em.find(Clinic.class, id);
            return clinic != null;
        } catch (PersistenceException e) {
            logger.error("Error validating clinic primary key with ID {}: {}", id, e.getMessage(), e);
            throw new JpaException(500, "Error validating clinic primary key.");
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }
}