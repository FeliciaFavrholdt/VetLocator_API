package dat.controllers.impl;

import dat.config.HibernateConfig;
import dat.controller.IController;
import dat.dao.impl.AppointmentDAO;
import dat.dto.AppointmentDTO;
import dat.exception.ApiException;
import dat.exceptions.JpaException;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class AppointmentController implements IController<AppointmentDTO, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(AppointmentController.class);
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
            logger.info("Reading appointment with ID: {}", id);
            AppointmentDTO appointmentDTO = dao.read(id);

            if (appointmentDTO != null) {
                ctx.status(200);
                ctx.json(appointmentDTO);
                logger.info("Appointment with ID: {} found and returned", id);
            } else {
                throw new ApiException(404, "Appointment not found");
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
            logger.info("Fetching all appointments");
            List<AppointmentDTO> appointmentDTOS = dao.readAll();
            ctx.status(200);
            ctx.json(appointmentDTOS);
            logger.info("All appointments fetched successfully, count: {}", appointmentDTOS.size());
        } catch (JpaException e) {
            logger.error("JPA Error fetching appointments: {}", e.getMessage(), e);
            throw new ApiException(500, "Error fetching appointments from database");
        } catch (Exception e) {
            logger.error("Error fetching all appointments: {}", e.getMessage(), e);
            throw new ApiException(500, "An unexpected error occurred on the server");
        }
    }

    @Override
    public void create(@NotNull Context ctx) {
        try {
            AppointmentDTO jsonRequest = ctx.bodyAsClass(AppointmentDTO.class);
            logger.info("Creating new appointment: {}", jsonRequest);
            AppointmentDTO appointmentDTO = dao.create(jsonRequest);
            ctx.status(201);
            ctx.json(appointmentDTO);
            logger.info("Appointment created successfully with ID: {}", appointmentDTO.getId());
        } catch (JpaException e) {
            logger.error("JPA Error creating appointment: {}", e.getMessage(), e);
            throw new ApiException(500, "Error creating appointment in the database");
        } catch (Exception e) {
            logger.error("Error creating appointment: {}", e.getMessage(), e);
            throw new ApiException(500, "An unexpected error occurred on the server");
        }
    }

    @Override
    public void update(@NotNull Context ctx) {
        try {
            int id = ctx.pathParamAsClass("id", Integer.class)
                    .check(this::validatePrimaryKey, "Not a valid id")
                    .get();
            logger.info("Updating appointment with ID: {}", id);
            AppointmentDTO appointmentDTO = dao.update(id, validateEntity(ctx));

            if (appointmentDTO != null) {
                ctx.status(200);
                ctx.json(appointmentDTO);
                logger.info("Appointment with ID: {} updated successfully", id);
            } else {
                throw new ApiException(404, "Appointment not found or update failed");
            }
        } catch (JpaException e) {
            logger.error("JPA Error updating appointment: {}", e.getMessage(), e);
            throw new ApiException(500, "Error updating appointment in the database");
        } catch (Exception e) {
            logger.error("Error updating appointment: {}", e.getMessage(), e);
            throw new ApiException(500, "An unexpected error occurred on the server");
        }
    }

    @Override
    public void delete(@NotNull Context ctx) {
        try {
            int id = ctx.pathParamAsClass("id", Integer.class)
                    .check(this::validatePrimaryKey, "Not a valid id")
                    .get();
            logger.info("Deleting appointment with ID: {}", id);
            dao.delete(id);
            ctx.status(204);
            logger.info("Appointment with ID: {} deleted successfully", id);
        } catch (JpaException e) {
            logger.error("JPA Error deleting appointment: {}", e.getMessage(), e);
            throw new ApiException(500, "Error deleting appointment from the database");
        } catch (Exception e) {
            logger.error("Error deleting appointment: {}", e.getMessage(), e);
            throw new ApiException(500, "An unexpected error occurred on the server");
        }
    }

    @Override
    public boolean validatePrimaryKey(Integer id) {
        try {
            boolean isValid = dao.validatePrimaryKey(id);
            if (!isValid) {
                throw new ApiException(400, "Invalid or missing primary key: " + id);
            }
            return isValid;
        } catch (JpaException e) {
            logger.error("JPA Error validating primary key: {}", e.getMessage(), e);
            throw new ApiException(500, "Database error during primary key validation");
        }
    }

    @Override
    public AppointmentDTO validateEntity(@NotNull Context ctx) {
        try {
            AppointmentDTO appointmentDTO = ctx.bodyValidator(AppointmentDTO.class)
                    .check(a -> a.getDate() != null, "Appointment date must be set")
                    .check(a -> a.getTime() != null, "Appointment time must be set")
                    .check(a -> a.getReason() != null && !a.getReason().isEmpty(), "Reason for appointment must be set")
                    .check(a -> a.getVeterinarianId() != null, "Veterinarian (Clinic) ID must be associated with the appointment")
                    .check(a -> a.getClientId() != null, "Client ID must be associated with the appointment")
                    .check(a -> a.getAnimalId() != null, "Animal ID must be associated with the appointment")
                    .get();
            logger.info("Appointment entity validated successfully: {}", appointmentDTO);
            return appointmentDTO;
        } catch (Exception e) {
            logger.error("Error validating appointment entity: {}", e.getMessage(), e);
            throw new ApiException(400, "Invalid or missing parameters in the appointment entity");
        }
    }
}