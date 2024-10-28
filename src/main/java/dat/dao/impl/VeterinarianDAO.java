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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class VeterinarianDAO implements IDAO<VeterinarianDTO, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(VeterinarianDAO.class);  // Logger instance
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
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Veterinarian veterinarian = veterinarianDTO.toEntity();

            Clinic clinic = em.find(Clinic.class, veterinarianDTO.getClinicId());
            if (clinic == null) {
                logger.warn("Clinic not found for clinicId: {}", veterinarianDTO.getClinicId());
                throw new JpaException(400, "Clinic not found for clinicId: " + veterinarianDTO.getClinicId());
            }
            veterinarian.setClinic(clinic);

            em.persist(veterinarian);
            em.getTransaction().commit();
            logger.info("Veterinarian created successfully with ID {}", veterinarian.getId());
            return new VeterinarianDTO(veterinarian);
        } catch (PersistenceException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            logger.error("Error creating veterinarian in the database: {}", e.getMessage());
            throw new JpaException(500, "Error creating veterinarian in the database.");
        } finally {
            em.close();
        }
    }

    @Override
    public VeterinarianDTO read(Integer id) {
        EntityManager em = emf.createEntityManager();
        try {
            Veterinarian veterinarian = em.find(Veterinarian.class, id);
            if (veterinarian == null) {
                logger.warn("Veterinarian not found for ID: {}", id);
                throw new JpaException(404, "Veterinarian not found for ID: " + id);
            }
            logger.info("Veterinarian with ID {} successfully retrieved.", id);
            return new VeterinarianDTO(veterinarian);
        } finally {
            em.close();
        }
    }

    @Override
    public List<VeterinarianDTO> readAll() {
        EntityManager em = emf.createEntityManager();
        try {
            List<VeterinarianDTO> veterinarians = em.createQuery("SELECT new dat.dto.VeterinarianDTO(v) FROM Veterinarian v", VeterinarianDTO.class)
                    .getResultList();
            logger.info("Successfully retrieved {} veterinarians.", veterinarians.size());
            return veterinarians;
        } finally {
            em.close();
        }
    }

    @Override
    public VeterinarianDTO update(Integer id, VeterinarianDTO veterinarianDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Veterinarian veterinarian = em.find(Veterinarian.class, id);
            if (veterinarian == null) {
                logger.warn("Veterinarian not found for ID: {}", id);
                throw new JpaException(404, "Veterinarian not found for ID: " + id);
            }
            veterinarian.convertFromDTO(veterinarianDTO);

            if (!veterinarian.getClinic().getId().equals(veterinarianDTO.getClinicId())) {
                Clinic clinic = em.find(Clinic.class, veterinarianDTO.getClinicId());
                if (clinic == null) {
                    logger.warn("Clinic not found for clinicId: {}", veterinarianDTO.getClinicId());
                    throw new JpaException(400, "Clinic not found for clinicId: " + veterinarianDTO.getClinicId());
                }
                veterinarian.setClinic(clinic);
            }

            Veterinarian mergedVeterinarian = em.merge(veterinarian);
            em.getTransaction().commit();
            logger.info("Veterinarian with ID {} successfully updated.", id);
            return new VeterinarianDTO(mergedVeterinarian);
        } catch (PersistenceException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            logger.error("Error updating veterinarian in the database: {}", e.getMessage());
            throw new JpaException(500, "Error updating veterinarian in the database.");
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(Integer id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Veterinarian veterinarian = em.find(Veterinarian.class, id);
            if (veterinarian == null) {
                logger.warn("Veterinarian not found for ID: {}", id);
                throw new JpaException(404, "Veterinarian not found for ID: " + id);
            }
            em.remove(veterinarian);
            em.getTransaction().commit();
            logger.info("Veterinarian with ID {} successfully deleted.", id);
        } catch (PersistenceException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            logger.error("Error deleting veterinarian from the database: {}", e.getMessage());
            throw new JpaException(500, "Error deleting veterinarian from the database.");
        } finally {
            em.close();
        }
    }

    @Override
    public boolean validatePrimaryKey(Integer id) {
        EntityManager em = emf.createEntityManager();
        try {
            boolean isValid = em.find(Veterinarian.class, id) != null;
            if (!isValid) {
                logger.warn("Invalid primary key: {}", id);
            }
            return isValid;
        } finally {
            em.close();
        }
    }
}