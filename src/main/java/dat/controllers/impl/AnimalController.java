package dat.controllers.impl;

import dat.config.HibernateConfig;
import dat.controllers.IController;
import dat.dao.impl.AnimalDAO;
import dat.dto.AnimalDTO;
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
                ctx.res().setStatus(200);
                ctx.json(animalDTO, AnimalDTO.class);
                logger.info("Animal with ID: {} found and returned", id);
            } else {
                ctx.res().setStatus(404);
                ctx.result("Animal not found");
                logger.warn("Animal with ID: {} not found", id);
            }
        } catch (Exception e) {
            logger.error("Error reading animal: {}", e.getMessage(), e);
            ctx.res().setStatus(500);
            ctx.result("Internal Server Error");
        }
    }

    @Override
    public void readAll(@NotNull Context ctx) {
        try {
            logger.info("Fetching all animals");
            List<AnimalDTO> animalDTOS = dao.readAll();
            ctx.res().setStatus(200);
            ctx.json(animalDTOS, AnimalDTO.class);
            logger.info("All animals fetched successfully, count: {}", animalDTOS.size());
        } catch (Exception e) {
            logger.error("Error fetching all animals: {}", e.getMessage(), e);
            ctx.res().setStatus(500);
            ctx.result("Internal Server Error");
        }
    }

    @Override
    public void create(@NotNull Context ctx) {
        try {
            AnimalDTO jsonRequest = ctx.bodyAsClass(AnimalDTO.class);
            logger.info("Creating new animal: {}", jsonRequest);
            AnimalDTO animalDTO = dao.create(jsonRequest);
            ctx.res().setStatus(201);
            ctx.json(animalDTO, AnimalDTO.class);
            logger.info("Animal created successfully with ID: {}", animalDTO.getId());
        } catch (Exception e) {
            logger.error("Error creating animal: {}", e.getMessage(), e);
            ctx.res().setStatus(500);
            ctx.result("Internal Server Error");
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
                ctx.res().setStatus(200);
                ctx.json(animalDTO, AnimalDTO.class);
                logger.info("Animal with ID: {} updated successfully", id);
            } else {
                ctx.res().setStatus(404);
                ctx.result("Animal not found or update failed");
                logger.warn("Animal with ID: {} not found or update failed", id);
            }
        } catch (Exception e) {
            logger.error("Error updating animal with ID: {}", e.getMessage(), e);
            ctx.res().setStatus(500);
            ctx.result("Internal Server Error");
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
            ctx.res().setStatus(204);
            logger.info("Animal with ID: {} deleted successfully", id);
        } catch (Exception e) {
            logger.error("Error deleting animal with ID: {}", e.getMessage(), e);
            ctx.res().setStatus(500);
            ctx.result("Internal Server Error");
        }
    }

    @Override
    public boolean validatePrimaryKey(Integer id) {
        try {
            boolean isValid = dao.validatePrimaryKey(id);
            if (!isValid) {
                logger.warn("Invalid primary key: {}", id);
            }
            return isValid;
        } catch (Exception e) {
            logger.error("Error validating primary key: {}", e.getMessage(), e);
            return false;
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
            throw e;  // Let the exception bubble up to be handled in the calling method.
        }
    }
}
