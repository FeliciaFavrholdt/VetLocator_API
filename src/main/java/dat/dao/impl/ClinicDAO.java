package dat.dao.impl;

import dat.dao.IDAO;
import dat.dto.ClinicDTO;
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
public class ClinicDAO implements IDAO<ClinicDTO, Integer> {

    private static ClinicDAO instance;
    private static EntityManagerFactory emf;

    public ClinicDAO(EntityManagerFactory emf) {
        ClinicDAO.emf = emf;
    }

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
            Clinic clinic = clinicDTO.toEntity();
            em.persist(clinic);
            em.getTransaction().commit();
            return new ClinicDTO(clinic);
        } catch (PersistenceException e) {
            throw new JpaException(500, "Error creating clinic in the database.");
        }
    }

    @Override
    public ClinicDTO read(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            Clinic clinic = em.find(Clinic.class, id);
            if (clinic == null) {
                throw new JpaException(404, "Clinic not found for ID: " + id);
            }
            return new ClinicDTO(clinic);
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
            Clinic clinic = em.find(Clinic.class, id);
            if (clinic == null) {
                throw new JpaException(404, "Clinic not found for ID: " + id);
            }
            clinic.updateFromDTO(clinicDTO);
            Clinic mergedClinic = em.merge(clinic);
            em.getTransaction().commit();
            return new ClinicDTO(mergedClinic);
        } catch (PersistenceException e) {
            throw new JpaException(500, "Error updating clinic in the database.");
        }
    }

    @Override
    public void delete(Integer id) {
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
    public boolean validatePrimaryKey(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(Clinic.class, id) != null;
        }
    }
}