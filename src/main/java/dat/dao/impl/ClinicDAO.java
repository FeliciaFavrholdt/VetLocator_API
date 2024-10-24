package dat.dao.impl;

import dat.dao.IDAO;
import dat.dto.ClinicDTO;
import dat.entities.Clinic;
import dat.entities.City;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

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
                // Find the city based on cityName and postalCode in ClinicDTO
                TypedQuery<City> cityQuery = em.createQuery("SELECT c FROM City c WHERE c.cityName = :cityName AND c.postalCode = :postalCode", City.class);
                cityQuery.setParameter("cityName", clinicDTO.getCityName());
                cityQuery.setParameter("postalCode", clinicDTO.getPostalCode());
                City city = cityQuery.getSingleResult();

                // Update the Clinic entity with the new DTO data
                clinic.updateFromDTO(clinicDTO, city);

                // Merge the updated entity and commit
                Clinic mergedClinic = em.merge(clinic);
                em.getTransaction().commit();

                return new ClinicDTO(mergedClinic);
            }
            return null;
        }
    }

    @Override
    public boolean delete(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Clinic clinic = em.find(Clinic.class, id);
            if (clinic != null) {
                em.remove(clinic);
            }
            em.getTransaction().commit();
        }
        return false;
    }

    @Override
    public boolean validatePrimaryKey(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            Clinic clinic = em.find(Clinic.class, id);
            return clinic != null;
        }
    }
}
