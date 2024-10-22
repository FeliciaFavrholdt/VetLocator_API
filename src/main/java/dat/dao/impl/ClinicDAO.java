package dat.dao.impl;

import dat.dao.IDAO;
import dat.dto.ClinicDTO;
import dat.entities.Clinic;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
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
            Clinic clinic = new Clinic(clinicDTO);
            em.persist(clinic);
            em.getTransaction().commit();
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
            Clinic clinic = em.find(Clinic.class, id);
            if (clinic != null) {
                clinic.updateFromDTO(clinicDTO);
                Clinic mergedClinic = em.merge(clinic);
                em.getTransaction().commit();
                return new ClinicDTO(mergedClinic);
            }
            return null;
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