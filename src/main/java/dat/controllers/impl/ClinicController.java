package dat.controllers.impl;

import dat.config.HibernateConfig;
import dat.controllers.IController;
import dat.dao.impl.ClinicDAO;
import dat.dto.ClinicDTO;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Controller class for Clinic entity.
 *
 * This class includes validation logic for both primary keys and entity data using bodyValidator and pathParamAsClass.
 * It handles the HTTP response by setting appropriate status codes and returning the result in JSON format.
 */

public class ClinicController implements IController<ClinicDTO, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(ClinicController.class);
    private final ClinicDAO dao;

    public ClinicController() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        this.dao = ClinicDAO.getInstance(emf);
    }

    @Override
    public void read(@NotNull Context ctx) {
        try {
            Integer id = ctx.pathParamAsClass("id", Integer.class)
                    .check(this::validatePrimaryKey, "Not a valid id")
                    .get();
            logger.info("Reading clinic with ID: {}", id);
            ClinicDTO clinicDTO = dao.read(id);

            if (clinicDTO != null) {
                ctx.res().setStatus(200);
                ctx.json(clinicDTO, ClinicDTO.class);
                logger.info("Clinic with ID: {} found and returned", id);
            } else {
                ctx.res().setStatus(404);
                ctx.result("Clinic not found");
                logger.warn("Clinic with ID: {} not found", id);
            }
        } catch (Exception e) {
            logger.error("Error reading clinic with ID: {}", e.getMessage(), e);
            ctx.res().setStatus(500);
            ctx.result("Internal Server Error");
        }
    }

    @Override
    public void readAll(@NotNull Context ctx) {
        try {
            logger.info("Fetching all clinics");
            List<ClinicDTO> clinicDTOS = dao.readAll();
            ctx.res().setStatus(200);
            ctx.json(clinicDTOS, ClinicDTO.class);
            logger.info("All clinics fetched successfully, count: {}", clinicDTOS.size());
        } catch (Exception e) {
            logger.error("Error fetching all clinics: {}", e.getMessage(), e);
            ctx.res().setStatus(500);
            ctx.result("Internal Server Error");
        }
    }

    @Override
    public void create(@NotNull Context ctx) {
        try {
            ClinicDTO jsonRequest = ctx.bodyAsClass(ClinicDTO.class);
            logger.info("Creating new clinic: {}", jsonRequest);
            ClinicDTO clinicDTO = dao.create(jsonRequest);
            ctx.res().setStatus(201);
            ctx.json(clinicDTO, ClinicDTO.class);
            logger.info("Clinic created successfully with ID: {}", clinicDTO.getId());
        } catch (Exception e) {
            logger.error("Error creating clinic: {}", e.getMessage(), e);
            ctx.res().setStatus(500);
            ctx.result("Internal Server Error");
        }
    }

    @Override
    public void update(@NotNull Context ctx) {
        try {
            Integer id = ctx.pathParamAsClass("id", Integer.class)
                    .check(this::validatePrimaryKey, "Not a valid id")
                    .get();
            logger.info("Updating clinic with ID: {}", id);
            ClinicDTO clinicDTO = dao.update(id, validateEntity(ctx));

            if (clinicDTO != null) {
                ctx.res().setStatus(200);
                ctx.json(clinicDTO, ClinicDTO.class);
                logger.info("Clinic with ID: {} updated successfully", id);
            } else {
                ctx.res().setStatus(404);
                ctx.result("Clinic not found or update failed");
                logger.warn("Clinic with ID: {} not found or update failed", id);
            }
        } catch (Exception e) {
            logger.error("Error updating clinic with ID: {}", e.getMessage(), e);
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
            logger.info("Deleting clinic with ID: {}", id);
            dao.delete(id);
            ctx.res().setStatus(204);
            logger.info("Clinic with ID: {} deleted successfully", id);
        } catch (Exception e) {
            logger.error("Error deleting clinic with ID: {}", e.getMessage(), e);
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
    public ClinicDTO validateEntity(@NotNull Context ctx) {
        try {
            ClinicDTO clinicDTO = ctx.bodyValidator(ClinicDTO.class)
                    .check(c -> c.getClinicName() != null && !c.getClinicName().isEmpty(), "Clinic name must be set")
                    .check(c -> c.getSpecialization() != null, "Clinic specialization must be set")
                    .check(c -> c.getPhone() != null && !c.getPhone().isEmpty(), "Phone number must be set")
                    .check(c -> c.getEmail() != null && !c.getEmail().isEmpty(), "Email must be set")
                    .check(c -> c.getAddress() != null && !c.getAddress().isEmpty(), "Clinic address must be set")
                    .check(c -> c.getCityName() != null && !c.getCityName().isEmpty(), "City name must be set")
                    .check(c -> c.getPostalCode() > 0, "Postal code must be set and greater than zero")
                    .get();
            logger.info("Clinic entity validated successfully: {}", clinicDTO);
            return clinicDTO;
        } catch (Exception e) {
            logger.error("Error validating clinic entity: {}", e.getMessage(), e);
            throw e;  // Let the exception bubble up to be handled in the calling method.
        }
    }
}