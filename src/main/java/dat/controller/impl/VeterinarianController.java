package dat.controllers.impl;

import dat.config.HibernateConfig;
import dat.controllers.IController;
import dat.dao.impl.VeterinarianDAO;
import dat.dto.VeterinarianDTO;
import dat.exceptions.ApiException;
import dat.exceptions.JpaException;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import jakarta.persistence.EntityManagerFactory;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class VeterinarianController implements IController<VeterinarianDTO, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(VeterinarianController.class);  // Logger instance
    private final VeterinarianDAO dao;

    public VeterinarianController() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        this.dao = VeterinarianDAO.getInstance(emf);
    }

    @Override
    public void read(@NotNull Context ctx) {
        try {
            int id = ctx.pathParamAsClass("id", Integer.class)
                    .check(this::validatePrimaryKey, "Not a valid id")
                    .get();
            VeterinarianDTO veterinarianDTO = dao.read(id);

            if (veterinarianDTO != null) {
                ctx.status(HttpStatus.OK);  // 200 OK
                ctx.json(veterinarianDTO);
                logger.info("Veterinarian with ID {} successfully retrieved.", id);
            } else {
                logger.warn("Veterinarian with ID {} not found.", id);
                throw new ApiException(HttpStatus.NOT_FOUND.getCode(), "Veterinarian not found");
            }
        } catch (ApiException e) {
            logger.error("API Exception while fetching veterinarian: {}", e.getMessage());
            ctx.status(e.getStatusCode());
            ctx.json(e.getMessageRecord());
        } catch (JpaException e) {
            logger.error("JPA Exception while fetching veterinarian: {}", e.getMessage());
            ctx.status(e.getStatusCode());
            ctx.json(e.getMessageRecord());
        } catch (Exception e) {
            logger.error("Unexpected error occurred while fetching veterinarian: {}", e.getMessage());
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR.getCode(), "Internal Server Error");
        }
    }

    @Override
    public void readAll(@NotNull Context ctx) {
        try {
            List<VeterinarianDTO> veterinarianDTOS = dao.readAll();
            ctx.status(HttpStatus.OK);  // 200 OK
            ctx.json(veterinarianDTOS);
            logger.info("Successfully fetched all veterinarians.");
        } catch (JpaException e) {
            logger.error("JPA Exception while fetching all veterinarians: {}", e.getMessage());
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR.getCode(), "Error fetching veterinarians from the database");
        } catch (Exception e) {
            logger.error("Unexpected error occurred while fetching veterinarians: {}", e.getMessage());
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR.getCode(), "An unexpected error occurred on the server");
        }
    }

    @Override
    public void create(@NotNull Context ctx) {
        try {
            VeterinarianDTO jsonRequest = ctx.bodyAsClass(VeterinarianDTO.class);
            VeterinarianDTO veterinarianDTO = dao.create(jsonRequest);
            ctx.status(HttpStatus.CREATED);  // 201 Created
            ctx.json(veterinarianDTO);
            logger.info("Successfully created new veterinarian with ID {}", veterinarianDTO.getId());
        } catch (JpaException e) {
            logger.error("JPA Exception while creating veterinarian: {}", e.getMessage());
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR.getCode(), "Error creating veterinarian in the database");
        } catch (Exception e) {
            logger.error("Unexpected error occurred while creating veterinarian: {}", e.getMessage());
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR.getCode(), "An unexpected error occurred");
        }
    }

    @Override
    public void update(@NotNull Context ctx) {
        try {
            int id = ctx.pathParamAsClass("id", Integer.class)
                    .check(this::validatePrimaryKey, "Not a valid id")
                    .get();
            VeterinarianDTO veterinarianDTO = dao.update(id, validateEntity(ctx));

            if (veterinarianDTO != null) {
                ctx.status(HttpStatus.OK);  // 200 OK
                ctx.json(veterinarianDTO);
                logger.info("Veterinarian with ID {} successfully updated.", id);
            } else {
                logger.warn("Veterinarian with ID {} not found or update failed.", id);
                throw new ApiException(HttpStatus.NOT_FOUND.getCode(), "Veterinarian not found or update failed");
            }
        } catch (JpaException e) {
            logger.error("JPA Exception while updating veterinarian: {}", e.getMessage());
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR.getCode(), "Error updating veterinarian in the database");
        } catch (Exception e) {
            logger.error("Unexpected error occurred while updating veterinarian: {}", e.getMessage());
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
            logger.info("Veterinarian with ID {} successfully deleted.", id);
        } catch (JpaException e) {
            logger.error("JPA Exception while deleting veterinarian: {}", e.getMessage());
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR.getCode(), "Error deleting veterinarian from the database");
        } catch (Exception e) {
            logger.error("Unexpected error occurred while deleting veterinarian: {}", e.getMessage());
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
    public VeterinarianDTO validateEntity(@NotNull Context ctx) {
        try {
            VeterinarianDTO veterinarianDTO = ctx.bodyValidator(VeterinarianDTO.class)
                    .check(v -> v.getName() != null && !v.getName().isEmpty(), "Veterinarian name must be set")
                    // Add any other fields you want to validate here
                    .get();
            return veterinarianDTO;
        } catch (Exception e) {
            logger.error("Invalid or missing parameters in the veterinarian entity: {}", e.getMessage());
            throw new ApiException(HttpStatus.BAD_REQUEST.getCode(), "Invalid or missing parameters in the veterinarian entity");
        }
    }
}