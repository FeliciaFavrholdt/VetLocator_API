package dat.controllers.impl;

import dat.config.HibernateConfig;
import dat.controllers.IController;
import dat.dao.impl.AnimalDAO;
import dat.dto.AnimalDTO;
import dat.exceptions.ApiException;
import dat.exceptions.JpaException;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class AnimalController implements IController<AnimalDTO, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(AnimalController.class);
    private final AnimalDAO dao;

    public AnimalController() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        this.dao = AnimalDAO.getInstance(emf);
    }

    @Override
    public void read(@NotNull Context ctx) {
        try {
            int id = ctx.pathParamAsClass("id", Integer.class)
                    .check(this::validatePrimaryKey, "Not a valid id")
                    .get();
            logger.info("Reading animal with ID: {}", id);
            AnimalDTO animalDTO = dao.read(id);

            if (animalDTO != null) {
                ctx.status(200);
                ctx.json(animalDTO);
                logger.info("Animal with ID: {} found and returned", id);
            } else {
                throw new ApiException(404, "Animal not found");
            }
        } catch (ApiException e) {
            logger.warn("API Error: {}", e.getMessage(), e);
            ctx.status(e.getStatusCode());
            ctx.json(e.getMessageRecord());
        } catch (JpaException e) {
            logger.error("JPA Error: {}", e.getMessage(), e);
            ctx.status(e.getStatusCode());
            ctx.json(e.getMessageRecord());
        } catch (Exception e) {
            logger.error("General Error: {}", e.getMessage(), e);
            throw new ApiException(500, "Internal Server Error");
        }
    }

    @Override
    public void readAll(@NotNull Context ctx) {
        try {
            logger.info("Fetching all animals");
            List<AnimalDTO> animalDTOS = dao.readAll();
            ctx.status(200);
            ctx.json(animalDTOS);
            logger.info("All animals fetched successfully, count: {}", animalDTOS.size());
        } catch (JpaException e) {
            logger.error("JPA Error fetching animals: {}", e.getMessage(), e);
            throw new ApiException(500, "Error fetching animals from database");
        } catch (Exception e) {
            logger.error("Error fetching all animals: {}", e.getMessage(), e);
            throw new ApiException(500, "Internal Server Error");
        }
    }

    @Override
    public void create(@NotNull Context ctx) {
        try {
            AnimalDTO jsonRequest = ctx.bodyAsClass(AnimalDTO.class);
            logger.info("Creating new animal: {}", jsonRequest);
            AnimalDTO animalDTO = dao.create(jsonRequest);
            ctx.status(201);
            ctx.json(animalDTO);
            logger.info("Animal created successfully with ID: {}", animalDTO.getId());
        } catch (JpaException e) {
            logger.error("JPA Error creating animal: {}", e.getMessage(), e);
            throw new ApiException(500, "Error creating animal in the database");
        } catch (Exception e) {
            logger.error("Error creating animal: {}", e.getMessage(), e);
            throw new ApiException(500, "Internal Server Error");
        }
    }

    @Override
    public void update(@NotNull Context ctx) {
        try {
            int id = ctx.pathParamAsClass("id", Integer.class)
                    .check(this::validatePrimaryKey, "Not a valid id")
                    .get();
            logger.info("Updating animal with ID: {}", id);
            AnimalDTO animalDTO = dao.update(id, validateEntity(ctx));

            if (animalDTO != null) {
                ctx.status(200);
                ctx.json(animalDTO);
                logger.info("Animal with ID: {} updated successfully", id);
            } else {
                throw new ApiException(404, "Animal not found or update failed");
            }
        } catch (JpaException e) {
            logger.error("JPA Error updating animal: {}", e.getMessage(), e);
            throw new ApiException(500, "Error updating animal in the database");
        } catch (Exception e) {
            logger.error("Error updating animal: {}", e.getMessage(), e);
            throw new ApiException(500, "Internal Server Error");
        }
    }

    @Override
    public void delete(@NotNull Context ctx) {
        try {
            int id = ctx.pathParamAsClass("id", Integer.class)
                    .check(this::validatePrimaryKey, "Not a valid id")
                    .get();
            logger.info("Deleting animal with ID: {}", id);
            dao.delete(id);
            ctx.status(204);
            logger.info("Animal with ID: {} deleted successfully", id);
        } catch (JpaException e) {
            logger.error("JPA Error deleting animal: {}", e.getMessage(), e);
            throw new ApiException(500, "Error deleting animal from the database");
        } catch (Exception e) {
            logger.error("Error deleting animal: {}", e.getMessage(), e);
            throw new ApiException(500, "Internal Server Error");
        }
    }

    @Override
    public boolean validatePrimaryKey(Integer id) {
        try {
            boolean isValid = dao.validatePrimaryKey(id);
            if (!isValid) {
                throw new ApiException(400, "Invalid primary key: " + id);
            }
            return isValid;
        } catch (JpaException e) {
            logger.error("JPA Error validating primary key: {}", e.getMessage(), e);
            throw new ApiException(500, "Database error during primary key validation");
        }
    }

    @Override
    public AnimalDTO validateEntity(@NotNull Context ctx) {
        try {
            AnimalDTO animalDTO = ctx.bodyValidator(AnimalDTO.class)
                    .check(a -> a.getName() != null && !a.getName().isEmpty(), "Animal name must be set")
                    .check(a -> a.getSpecies() != null && !a.getSpecies().isEmpty(), "Animal species must be set")
                    .check(a -> a.getAge() >= 0, "Animal age must be a non-negative number")
                    .check(a -> a.getUserId() != null, "Owner (user) ID must be set")
                    .get();
            logger.info("Animal entity validated successfully: {}", animalDTO);
            return animalDTO;
        } catch (Exception e) {
            logger.error("Error validating animal entity: {}", e.getMessage(), e);
            throw new ApiException(400, "Invalid animal entity");
        }
    }
}