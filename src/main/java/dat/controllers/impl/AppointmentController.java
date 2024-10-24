package dat.controllers.impl;

import dat.config.HibernateConfig;
import dat.controllers.IController;
import dat.dao.impl.AppointmentDAO;
import dat.dto.AppointmentDTO;
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
                ctx.res().setStatus(200);
                ctx.json(appointmentDTO, AppointmentDTO.class);
                logger.info("Appointment with ID: {} found and returned", id);
            } else {
                ctx.res().setStatus(404);
                ctx.result("Appointment not found");
                logger.warn("Appointment with ID: {} not found", id);
            }
        } catch (Exception e) {
            logger.error("Error reading appointment with ID: {}", e.getMessage(), e);
            ctx.res().setStatus(500);
            ctx.result("Internal Server Error");
        }
    }

    @Override
    public void readAll(@NotNull Context ctx) {
        try {
            logger.info("Fetching all appointments");
            List<AppointmentDTO> appointmentDTOS = dao.readAll();
            ctx.res().setStatus(200);
            ctx.json(appointmentDTOS, AppointmentDTO.class);
            logger.info("All appointments fetched successfully, count: {}", appointmentDTOS.size());
        } catch (Exception e) {
            logger.error("Error fetching all appointments: {}", e.getMessage(), e);
            ctx.res().setStatus(500);
            ctx.result("Internal Server Error");
        }
    }

    @Override
    public void create(@NotNull Context ctx) {
        try {
            AppointmentDTO jsonRequest = ctx.bodyAsClass(AppointmentDTO.class);
            logger.info("Creating new appointment: {}", jsonRequest);
            AppointmentDTO appointmentDTO = dao.create(jsonRequest);
            ctx.res().setStatus(201);
            ctx.json(appointmentDTO, AppointmentDTO.class);
            logger.info("Appointment created successfully with ID: {}", appointmentDTO.getId());
        } catch (Exception e) {
            logger.error("Error creating appointment: {}", e.getMessage(), e);
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
            logger.info("Updating appointment with ID: {}", id);
            AppointmentDTO appointmentDTO = dao.update(id, validateEntity(ctx));

            if (appointmentDTO != null) {
                ctx.res().setStatus(200);
                ctx.json(appointmentDTO, AppointmentDTO.class);
                logger.info("Appointment with ID: {} updated successfully", id);
            } else {
                ctx.res().setStatus(404);
                ctx.result("Appointment not found or update failed");
                logger.warn("Appointment with ID: {} not found or update failed", id);
            }
        } catch (Exception e) {
            logger.error("Error updating appointment with ID: {}", e.getMessage(), e);
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
            logger.info("Deleting appointment with ID: {}", id);
            dao.delete(id);
            ctx.res().setStatus(204);
            logger.info("Appointment with ID: {} deleted successfully", id);
        } catch (Exception e) {
            logger.error("Error deleting appointment with ID: {}", e.getMessage(), e);
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
            throw e;  // Let the exception bubble up to be handled in the calling method.
        }
    }
}
