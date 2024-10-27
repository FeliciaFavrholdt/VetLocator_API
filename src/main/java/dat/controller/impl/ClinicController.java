package dat.controllers.impl;

import dat.config.HibernateConfig;
import dat.controllers.IController;
import dat.dao.impl.ClinicDAO;
import dat.dto.ClinicDTO;
import dat.exceptions.ApiException;
import dat.exceptions.JpaException;
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
                ctx.status(200);
                ctx.json(clinicDTO);
                logger.info("Clinic with ID: {} found and returned", id);
            } else {
                throw new ApiException(404, "Clinic not found");
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
            logger.info("Fetching all clinics");
            List<ClinicDTO> clinicDTOS = dao.readAll();
            ctx.status(200);
            ctx.json(clinicDTOS);
            logger.info("All clinics fetched successfully, count: {}", clinicDTOS.size());
        } catch (JpaException e) {
            logger.error("JPA Error fetching clinics: {}", e.getMessage(), e);
            throw new ApiException(500, "Error fetching clinics from database");
        } catch (Exception e) {
            logger.error("Error fetching all clinics: {}", e.getMessage(), e);
            throw new ApiException(500, "Internal Server Error");
        }
    }

    @Override
    public void create(@NotNull Context ctx) {
        try {
            ClinicDTO jsonRequest = ctx.bodyAsClass(ClinicDTO.class);
            logger.info("Creating new clinic: {}", jsonRequest);
            ClinicDTO clinicDTO = dao.create(jsonRequest);
            ctx.status(201);
            ctx.json(clinicDTO);
            logger.info("Clinic created successfully with ID: {}", clinicDTO.getId());
        } catch (JpaException e) {
            logger.error("JPA Error creating clinic: {}", e.getMessage(), e);
            throw new ApiException(500, "Error creating clinic in the database");
        } catch (Exception e) {
            logger.error("Error creating clinic: {}", e.getMessage(), e);
            throw new ApiException(500, "Internal Server Error");
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
                ctx.status(200);
                ctx.json(clinicDTO);
                logger.info("Clinic with ID: {} updated successfully", id);
            } else {
                throw new ApiException(404, "Clinic not found or update failed");
            }
        } catch (JpaException e) {
            logger.error("JPA Error updating clinic: {}", e.getMessage(), e);
            throw new ApiException(500, "Error updating clinic in the database");
        } catch (Exception e) {
            logger.error("Error updating clinic: {}", e.getMessage(), e);
            throw new ApiException(500, "Internal Server Error");
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
            ctx.status(204);
            logger.info("Clinic with ID: {} deleted successfully", id);
        } catch (JpaException e) {
            logger.error("JPA Error deleting clinic: {}", e.getMessage(), e);
            throw new ApiException(500, "Error deleting clinic from the database");
        } catch (Exception e) {
            logger.error("Error deleting clinic: {}", e.getMessage(), e);
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
    public ClinicDTO validateEntity(@NotNull Context ctx) {
        try {
            ClinicDTO clinicDTO = ctx.bodyValidator(ClinicDTO.class)
                    .check(c -> c.getClinicName() != null && !c.getClinicName().isEmpty(), "Clinic name must be set")
                    .check(c -> c.getSpecialization() != null, "Clinic specialization must be set")
                    .check(c -> c.getPhone() != null && !c.getPhone().isEmpty(), "Phone number must be set")
                    .check(c -> c.getEmail() != null && !c.getEmail().isEmpty(), "Email must be set")
                    .check(c -> c.getAddress() != null && !c.getAddress().isEmpty(), "Clinic address must be set")
                    .check(c -> c.getPostalCode() > 0, "Postal code must be set and greater than zero")
                    .get();
            logger.info("Clinic entity validated successfully: {}", clinicDTO);
            return clinicDTO;
        } catch (Exception e) {
            logger.error("Error validating clinic entity: {}", e.getMessage(), e);
            throw new ApiException(400, "Invalid clinic entity");
        }
    }

    // Endpoint to get clinics on duty

    public void getOnDutyClinics(Context ctx) throws ApiException {
        try {
            List<ClinicDTO> onDutyClinics = dao.onDutyClinics(); // Hent data fra DAO
            ctx.json(onDutyClinics); // Send resultatet som JSON-svar til klienten
        } catch (ApiException e) {
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiException(500, "An unexpected error occurred while fetching on-duty clinics");
        }
    }


}