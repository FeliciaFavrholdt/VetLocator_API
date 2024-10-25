package dat.dao.impl;

import dat.dao.IDAO;
import dat.dto.ClinicDTO;
import dat.entities.Clinic;
import dat.entities.City;
import dat.enums.Weekday;
import dat.exceptions.ApiException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class ClinicDAO implements IDAO<ClinicDTO, Integer> {

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
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            // Find City based on cityName and postalCode from ClinicDTO
            TypedQuery<City> cityQuery = em.createQuery("SELECT c FROM City c WHERE c.cityName = :cityName AND c.postalCode = :postalCode", City.class);
            cityQuery.setParameter("cityName", clinicDTO.getCityName());
            cityQuery.setParameter("postalCode", clinicDTO.getPostalCode());
            City city = cityQuery.getSingleResult();

            // Convert DTO to Clinic entity and set the city
            Clinic clinic = new Clinic(clinicDTO, city);

            em.persist(clinic);
            em.getTransaction().commit();

            // Return the persisted Clinic entity as a DTO
            return new ClinicDTO(clinic);
        }
    }

    @Override
    public ClinicDTO read(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            Clinic clinic = em.find(Clinic.class, id);
            return clinic != null ? new ClinicDTO(clinic) : null;
        }
    }

    @Override
    public List<ClinicDTO> readAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<ClinicDTO> query = em.createQuery("SELECT new dat.dto.ClinicDTO(c) FROM Clinic c", ClinicDTO.class);
            return query.getResultList();
        }
    }

    @Override
    public ClinicDTO update(Integer id, ClinicDTO clinicDTO) {

        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            // Find the existing Clinic by ID
            Clinic clinic = em.find(Clinic.class, id);
            if (clinic != null) {
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
            }
            return null;  // Clinic not found

        } catch (Exception e) {
            e.printStackTrace();
            throw e;

        }
    }

    @Override
    public void delete(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Clinic clinic = em.find(Clinic.class, id);
            if (clinic != null) {
                em.remove(clinic);
            }
            em.getTransaction().commit();
        }
    }

    @Override
    public boolean validatePrimaryKey(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            Clinic clinic = em.find(Clinic.class, id);
            return clinic != null;
        }
    }
}
