package dat.dao.impl;

import dat.dao.IDAO;
import dat.dto.VeterinarianDTO;
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
public class VeterinarianDAO implements IDAO<VeterinarianDTO, Integer> {

    private static VeterinarianDAO instance;
    private static EntityManagerFactory emf;

    public VeterinarianDAO(EntityManagerFactory emf) {
        VeterinarianDAO.emf = emf;
    }

    public static VeterinarianDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new VeterinarianDAO();
        }
        return instance;
    }

    @Override
    public VeterinarianDTO create(VeterinarianDTO veterinarianDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Veterinarian veterinarian = veterinarianDTO.toEntity();

            Clinic clinic = em.find(Clinic.class, veterinarianDTO.getClinicId());
            if (clinic == null) {
                throw new JpaException(400, "Clinic not found for clinicId: " + veterinarianDTO.getClinicId());
            }
            veterinarian.setClinic(clinic);

            em.persist(veterinarian);
            em.getTransaction().commit();
            return new VeterinarianDTO(veterinarian);
        } catch (PersistenceException e) {
            throw new JpaException(500, "Error creating veterinarian in the database.");
        }
    }

    @Override
    public VeterinarianDTO read(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            Veterinarian veterinarian = em.find(Veterinarian.class, id);
            if (veterinarian == null) {
                throw new JpaException(404, "Veterinarian not found for ID: " + id);
            }
            return new VeterinarianDTO(veterinarian);
        }
    }

    @Override
    public List<VeterinarianDTO> readAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<VeterinarianDTO> query = em.createQuery("SELECT new dat.dto.VeterinarianDTO(v) FROM Veterinarian v", VeterinarianDTO.class);
            return query.getResultList();
        }
    }

    @Override
    public VeterinarianDTO update(Integer id, VeterinarianDTO veterinarianDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Veterinarian veterinarian = em.find(Veterinarian.class, id);
            if (veterinarian == null) {
                throw new JpaException(404, "Veterinarian not found for ID: " + id);
            }
            veterinarian.convertFromDTO(veterinarianDTO);

            if (!veterinarian.getClinic().getId().equals(veterinarianDTO.getClinicId())) {
                Clinic clinic = em.find(Clinic.class, veterinarianDTO.getClinicId());
                if (clinic == null) {
                    throw new JpaException(400, "Clinic not found for clinicId: " + veterinarianDTO.getClinicId());
                }
                veterinarian.setClinic(clinic);
            }

            Veterinarian mergedVeterinarian = em.merge(veterinarian);
            em.getTransaction().commit();
            return new VeterinarianDTO(mergedVeterinarian);
        } catch (PersistenceException e) {
            throw new JpaException(500, "Error updating veterinarian in the database.");
        }
    }

    @Override
    public void delete(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Veterinarian veterinarian = em.find(Veterinarian.class, id);
            if (veterinarian == null) {
                throw new JpaException(404, "Veterinarian not found for ID: " + id);
            }
            em.remove(veterinarian);
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            throw new JpaException(500, "Error deleting veterinarian from the database.");
        }
    }

    @Override
    public boolean validatePrimaryKey(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(Veterinarian.class, id) != null;
        }
    }
}

