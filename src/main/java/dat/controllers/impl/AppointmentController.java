package dat.controllers.impl;

import dat.config.HibernateConfig;
import dat.controllers.IController;
import dat.dao.impl.AppointmentDAO;
import dat.dto.AppointmentDTO;
import dat.exceptions.ApiException;
import dat.exceptions.JpaException;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import jakarta.persistence.EntityManagerFactory;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class AppointmentController implements IController<AppointmentDTO, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(AppointmentController.class);  // Logger instance
    private final AppointmentDAO dao;

    public AppointmentController() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        this.dao = AppointmentDAO.getInstance(emf);
    }

    @Override
    public void read(@NotNull Context ctx) {
        try {
            int id = ctx.pathParamAsClass("id", Integer.class)
                    .check(this::validatePrimaryKey, "Not a valid id")
                    .get();
            AppointmentDTO appointmentDTO = dao.read(id);

            if (appointmentDTO != null) {
                ctx.status(HttpStatus.OK);
                ctx.json(appointmentDTO);
                logger.info("Appointment with ID {} successfully retrieved.", id);
            } else {
                logger.warn("Appointment with ID {} not found.", id);
                throw new ApiException(HttpStatus.NOT_FOUND.getCode(), "Appointment not found");
            }
        } catch (ApiException e) {
            logger.error("API Exception while fetching appointment: {}", e.getMessage());
            ctx.status(e.getStatusCode());
            ctx.json(e.getMessageRecord());
        } catch (JpaException e) {
            logger.error("JPA Exception while fetching appointment: {}", e.getMessage());
            ctx.status(e.getStatusCode());
            ctx.json(e.getMessageRecord());
        } catch (Exception e) {
            logger.error("Unexpected error occurred while fetching appointment: {}", e.getMessage());
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR.getCode(), "Internal Server Error");
        }
    }

    @Override
    public void readAll(@NotNull Context ctx) {
        try {
            List<AppointmentDTO> appointmentDTOS = dao.readAll();
            ctx.status(HttpStatus.OK);
            ctx.json(appointmentDTOS);
            logger.info("Successfully fetched all appointments.");
        } catch (JpaException e) {
            logger.error("JPA Exception while fetching all appointments: {}", e.getMessage());
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR.getCode(), "Error fetching appointments from the database");
        } catch (Exception e) {
            logger.error("Unexpected error occurred while fetching all appointments: {}", e.getMessage());
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR.getCode(), "An unexpected error occurred on the server");
        }
    }

    @Override
    public void create(@NotNull Context ctx) {
        try {
            AppointmentDTO jsonRequest = ctx.bodyAsClass(AppointmentDTO.class);
            AppointmentDTO appointmentDTO = dao.create(jsonRequest);
            ctx.status(HttpStatus.CREATED);  // 201 Created
            ctx.json(appointmentDTO);
            logger.info("Successfully created new appointment with ID {}", appointmentDTO.getId());
        } catch (JpaException e) {
            logger.error("JPA Exception while creating appointment: {}", e.getMessage());
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR.getCode(), "Error creating appointment in the database");
        } catch (Exception e) {
            logger.error("Unexpected error occurred while creating appointment: {}", e.getMessage());
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR.getCode(), "An unexpected error occurred");
        }
    }

    @Override
    public void update(@NotNull Context ctx) {
        try {
            int id = ctx.pathParamAsClass("id", Integer.class)
                    .check(this::validatePrimaryKey, "Not a valid id")
                    .get();
            AppointmentDTO appointmentDTO = dao.update(id, validateEntity(ctx));

            if (appointmentDTO != null) {
                ctx.status(HttpStatus.OK);  // 200 OK
                ctx.json(appointmentDTO);
                logger.info("Appointment with ID {} successfully updated.", id);
            } else {
                logger.warn("Appointment with ID {} not found or update failed.", id);
                throw new ApiException(HttpStatus.NOT_FOUND.getCode(), "Appointment not found or update failed");
            }
        } catch (JpaException e) {
            logger.error("JPA Exception while updating appointment: {}", e.getMessage());
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR.getCode(), "Error updating appointment in the database");
        } catch (Exception e) {
            logger.error("Unexpected error occurred while updating appointment: {}", e.getMessage());
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
            logger.info("Appointment with ID {} successfully deleted.", id);
        } catch (JpaException e) {
            logger.error("JPA Exception while deleting appointment: {}", e.getMessage());
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR.getCode(), "Error deleting appointment from the database");
        } catch (Exception e) {
            logger.error("Unexpected error occurred while deleting appointment: {}", e.getMessage());
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
    public AppointmentDTO validateEntity(@NotNull Context ctx) {
        try {
            AppointmentDTO appointmentDTO = ctx.bodyValidator(AppointmentDTO.class)
                    .check(a -> a.getAppointmentDateTime() != null, "Appointment date must be set")
                    .check(a -> a.getVeterinarianId() != null, "Veterinarian ID must be set")
                    .check(a -> a.getAnimalId() != null, "Animal ID must be set")
                    .get();
            return appointmentDTO;
        } catch (Exception e) {
            logger.error("Invalid or missing parameters in the appointment entity: {}", e.getMessage());
            throw new ApiException(HttpStatus.BAD_REQUEST.getCode(), "Invalid or missing parameters in the appointment entity");
        }
    }
}