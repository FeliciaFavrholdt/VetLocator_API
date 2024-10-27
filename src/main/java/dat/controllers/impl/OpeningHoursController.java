package dat.controllers.impl;

import dat.config.HibernateConfig;
import dat.controllers.IController;
import dat.dao.impl.OpeningHoursDAO;
import dat.dao.impl.ClinicDAO;
import dat.dto.OpeningHoursDTO;
import dat.entities.OpeningHours;
import dat.entities.Clinic;
import dat.exceptions.ApiException;
import dat.exceptions.JpaException;

import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class OpeningHoursController implements IController<OpeningHoursDTO, Long> {

    private static final Logger logger = LoggerFactory.getLogger(OpeningHoursController.class);
    private final OpeningHoursDAO openingHoursDAO;
    private final ClinicDAO clinicDAO;

    public OpeningHoursController() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        this.openingHoursDAO = OpeningHoursDAO.getInstance(emf);
        this.clinicDAO = ClinicDAO.getInstance(emf);
    }

    @Override
    public void read(@NotNull Context ctx) {
        try {
            Long id = ctx.pathParamAsClass("id", Long.class)
                    .check(this::validatePrimaryKey, "Not a valid ID")
                    .get();
            logger.info("Reading opening hours with ID: {}", id);
            OpeningHoursDTO openingHoursDTO = openingHoursDAO.read(id);

            if (openingHoursDTO != null) {
                ctx.status(200);
                ctx.json(openingHoursDTO);
                logger.info("Opening hours with ID: {} found and returned", id);
            } else {
                throw new ApiException(404, "Opening hours not found");
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
            logger.info("Reading all opening hours");
            List<OpeningHoursDTO> openingHoursDTOs = openingHoursDAO.readAll();

            if (openingHoursDTOs != null) {
                ctx.status(200);
                ctx.json(openingHoursDTOs);
                logger.info("All opening hours found and returned");
            } else {
                throw new ApiException(404, "No opening hours found");
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
    public void create(@NotNull Context ctx) {
    }

    @Override
    public void update(@NotNull Context ctx) {
    }

    @Override
    public void delete(@NotNull Context ctx) {
        try {
            Long id = ctx.pathParamAsClass("id", Long.class)
                    .check(this::validatePrimaryKey, "Not a valid ID")
                    .get();
            logger.info("Deleting opening hours with ID: {}", id);
            openingHoursDAO.delete(id);
            ctx.status(204);
            logger.info("Opening hours with ID: {} deleted successfully", id);
        } catch (JpaException e) {
            logger.error("JPA Error deleting opening hours: {}", e.getMessage(), e);
            throw new ApiException(500, "Error deleting opening hours from the database");
        } catch (Exception e) {
            logger.error("Error deleting opening hours: {}", e.getMessage(), e);
            throw new ApiException(500, "An unexpected error occurred on the server");
        }
    }

    @Override
    public boolean validatePrimaryKey(Long id) {
        try {
            boolean isValid = openingHoursDAO.validatePrimaryKey(id);
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
    public OpeningHoursDTO validateEntity(@NotNull Context ctx) {
        try {
            OpeningHoursDTO dto = ctx.bodyValidator(OpeningHoursDTO.class)
                    .check(o -> o.getWeekday() != null, "Weekday must be set")
                    .check(o -> o.getStartTime() != null, "Start time must be set")
                    .check(o -> o.getEndTime() != null, "End time must be set")
                    .check(o -> o.getVeterinaryClinicId() != null, "Veterinary clinic ID must be associated with the opening hours")
                    .get();
            logger.info("Opening hours entity validated successfully: {}", dto);
            return dto;
        } catch (Exception e) {
            logger.error("Error validating opening hours entity: {}", e.getMessage(), e);
            throw new ApiException(400, "Invalid or missing parameters in the opening hours entity");
        }
    }
}