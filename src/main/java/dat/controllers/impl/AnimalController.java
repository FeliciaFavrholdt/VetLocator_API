package dat.controllers.impl;

import dat.config.HibernateConfig;
import dat.controllers.IController;
import dat.dao.impl.AnimalDAO;
import dat.dto.AnimalDTO;
import dat.exceptions.ApiException;
import dat.exceptions.JpaException;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import jakarta.persistence.EntityManagerFactory;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class AnimalController implements IController<AnimalDTO, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(AnimalController.class);  // Logger instance
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
            AnimalDTO animalDTO = dao.read(id);

            if (animalDTO != null) {
                ctx.status(HttpStatus.OK);
                ctx.json(animalDTO);
                logger.info("Animal with ID {} successfully retrieved.", id);
            } else {
                logger.warn("Animal with ID {} not found.", id);
                throw new ApiException(HttpStatus.NOT_FOUND.getCode(), "Animal not found");
            }
        } catch (ApiException e) {
            logger.error("API Exception while fetching animal: {}", e.getMessage());
            ctx.status(e.getStatusCode());
            ctx.json(e.getMessageRecord());
        } catch (JpaException e) {
            logger.error("JPA Exception while fetching animal: {}", e.getMessage());
            ctx.status(e.getStatusCode());
            ctx.json(e.getMessageRecord());
        } catch (Exception e) {
            logger.error("Unexpected error occurred while fetching animal: {}", e.getMessage());
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR.getCode(), "Internal Server Error");
        }
    }

    @Override
    public void readAll(@NotNull Context ctx) {
        try {
            List<AnimalDTO> animalDTOS = dao.readAll();
            ctx.status(HttpStatus.OK);
            ctx.json(animalDTOS);
            logger.info("Successfully fetched all animals.");
        } catch (JpaException e) {
            logger.error("JPA Exception while fetching all animals: {}", e.getMessage());
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR.getCode(), "Error fetching animals from the database");
        } catch (Exception e) {
            logger.error("Unexpected error occurred while fetching all animals: {}", e.getMessage());
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR.getCode(), "An unexpected error occurred on the server");
        }
    }

    @Override
    public void create(@NotNull Context ctx) {
        try {
            AnimalDTO jsonRequest = ctx.bodyAsClass(AnimalDTO.class);
            AnimalDTO animalDTO = dao.create(jsonRequest);
            ctx.status(HttpStatus.CREATED);  // 201 Created
            ctx.json(animalDTO);
            logger.info("Successfully created new animal: {}", animalDTO.getName());
        } catch (JpaException e) {
            logger.error("JPA Exception while creating animal: {}", e.getMessage());
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR.getCode(), "Error creating animal in the database");
        } catch (Exception e) {
            logger.error("Unexpected error occurred while creating animal: {}", e.getMessage());
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR.getCode(), "An unexpected error occurred");
        }
    }

    @Override
    public void update(@NotNull Context ctx) {
        try {
            int id = ctx.pathParamAsClass("id", Integer.class)
                    .check(this::validatePrimaryKey, "Not a valid id")
                    .get();
            AnimalDTO animalDTO = dao.update(id, validateEntity(ctx));

            if (animalDTO != null) {
                ctx.status(HttpStatus.OK);  // 200 OK
                ctx.json(animalDTO);
                logger.info("Animal with ID {} successfully updated.", id);
            } else {
                logger.warn("Animal with ID {} not found or update failed.", id);
                throw new ApiException(HttpStatus.NOT_FOUND.getCode(), "Animal not found or update failed");
            }
        } catch (JpaException e) {
            logger.error("JPA Exception while updating animal: {}", e.getMessage());
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR.getCode(), "Error updating animal in the database");
        } catch (Exception e) {
            logger.error("Unexpected error occurred while updating animal: {}", e.getMessage());
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR.getCode(), "An unexpected error occurred on the server");
        }
    }

    @Override
    public void delete(@NotNull Context ctx) {
        try {
            int id = ctx.pathParamAsClass("id", Integer.class)
                    .check(this::validatePrimaryKey, "Not a valid id")
                    .get();
            dao.delete(id);
            ctx.status(HttpStatus.NO_CONTENT);  // 204 No Content
            logger.info("Animal with ID {} successfully deleted.", id);
        } catch (JpaException e) {
            logger.error("JPA Exception while deleting animal: {}", e.getMessage());
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR.getCode(), "Error deleting animal from the database");
        } catch (Exception e) {
            logger.error("Unexpected error occurred while deleting animal: {}", e.getMessage());
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR.getCode(), "An unexpected error occurred on the server");
        }
    }

    @Override
    public boolean validatePrimaryKey(Integer id) {
        try {
            boolean isValid = dao.validatePrimaryKey(id);
            if (!isValid) {
                logger.warn("Invalid primary key: {}", id);
                throw new ApiException(HttpStatus.BAD_REQUEST.getCode(), "Invalid primary key");
            }
            return isValid;
        } catch (JpaException e) {
            logger.error("JPA Exception during primary key validation: {}", e.getMessage());
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR.getCode(), "Database error during primary key validation");
        }
    }

    @Override
    public AnimalDTO validateEntity(@NotNull Context ctx) {
        try {
            AnimalDTO animalDTO = ctx.bodyValidator(AnimalDTO.class)
                    .check(a -> a.getName() != null && !a.getName().isEmpty(), "Animal name must be set")
                    .check(a -> a.getSpecies() != null && !a.getSpecies().isEmpty(), "Animal species must be set")
                    .check(a -> a.getAge() >= 0, "Animal age must be a non-negative number")
                    .check(a -> a.getOwnerId() != null, "Owner (user) ID must be set")
                    .get();
            return animalDTO;
        } catch (Exception e) {
            logger.error("Invalid or missing parameters in the animal entity: {}", e.getMessage());
            throw new ApiException(HttpStatus.BAD_REQUEST.getCode(), "Invalid or missing parameters in the animal entity");
        }
    }

    public void searchBySpecies(@NotNull Context context) {
    }

    public void searchByName(@NotNull Context context) {
    }

    public void getMedicalHistory(@NotNull Context context) {
    }

    public void addMedicalHistory(@NotNull Context context) {
    }

    public void getAnimalAppointments(@NotNull Context context) {
    }
}