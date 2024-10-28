package dat.controllers.impl;

import dat.config.HibernateConfig;
import dat.controllers.IController;
import dat.dao.impl.AppointmentDAO;
import dat.dao.impl.ClinicDAO;
import dat.dto.ClinicDTO;
import dat.entities.Appointment;
import dat.exceptions.ApiException;
import dat.exceptions.JpaException;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import jakarta.persistence.EntityManagerFactory;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class ClinicController implements IController<ClinicDTO, Long> {  // Primary key type changed to Long

    private static final Logger logger = LoggerFactory.getLogger(ClinicController.class);  // Logger instance
    private final ClinicDAO clinicDAO;
    private final AppointmentDAO appointmentDAO;

    public ClinicController() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        this.clinicDAO = ClinicDAO.getInstance(emf);
        this.appointmentDAO = AppointmentDAO.getInstance(emf);
    }

    @Override
    public void read(@NotNull Context ctx) {
        try {
            Long id = ctx.pathParamAsClass("id", Long.class)  // Changed to Long
                    .check(this::validatePrimaryKey, "Not a valid id")
                    .get();
            ClinicDTO clinicDTO = clinicDAO.read(id);

            if (clinicDTO != null) {
                ctx.status(HttpStatus.OK);  // 200 OK
                ctx.json(clinicDTO);
                logger.info("Clinic with ID {} successfully retrieved.", id);
            } else {
                logger.warn("Clinic with ID {} not found.", id);
                throw new ApiException(HttpStatus.NOT_FOUND.getCode(), "Clinic not found");
            }
        } catch (ApiException e) {
            logger.error("API Exception while fetching clinic: {}", e.getMessage());
            ctx.status(e.getStatusCode());
            ctx.json(e.getMessageRecord());
        } catch (JpaException e) {
            logger.error("JPA Exception while fetching clinic: {}", e.getMessage());
            ctx.status(e.getStatusCode());
            ctx.json(e.getMessageRecord());
        } catch (Exception e) {
            logger.error("Unexpected error occurred while fetching clinic: {}", e.getMessage());
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR.getCode(), "Internal Server Error");
        }
    }

    @Override
    public void readAll(@NotNull Context ctx) {
        try {
            List<ClinicDTO> clinicDTOS = clinicDAO.readAll();
            ctx.status(HttpStatus.OK);  // 200 OK
            ctx.json(clinicDTOS);
            logger.info("Successfully fetched all clinics.");
        } catch (JpaException e) {
            logger.error("JPA Exception while fetching all clinics: {}", e.getMessage());
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR.getCode(), "Error fetching clinics from database");
        } catch (Exception e) {
            logger.error("Unexpected error occurred while fetching clinics: {}", e.getMessage());
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR.getCode(), "An unexpected error occurred on the server");
        }
    }

    @Override
    public void create(@NotNull Context ctx) {
        try {
            ClinicDTO jsonRequest = ctx.bodyAsClass(ClinicDTO.class);
            ClinicDTO clinicDTO = clinicDAO.create(jsonRequest);
            ctx.status(HttpStatus.CREATED);  // 201 Created
            ctx.json(clinicDTO);
            logger.info("Successfully created new clinic with ID {}", clinicDTO.getId());
        } catch (JpaException e) {
            logger.error("JPA Exception while creating clinic: {}", e.getMessage());
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR.getCode(), "Error creating clinic in the database");
        } catch (Exception e) {
            logger.error("Unexpected error occurred while creating clinic: {}", e.getMessage());
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR.getCode(), "An unexpected error occurred");
        }
    }

    @Override
    public void update(@NotNull Context ctx) {
        try {
            Long id = ctx.pathParamAsClass("id", Long.class)  // Changed to Long
                    .check(this::validatePrimaryKey, "Not a valid id")
                    .get();
            ClinicDTO clinicDTO = clinicDAO.update(id, validateEntity(ctx));

            if (clinicDTO != null) {
                ctx.status(HttpStatus.OK);  // 200 OK
                ctx.json(clinicDTO);
                logger.info("Clinic with ID {} successfully updated.", id);
            } else {
                logger.warn("Clinic with ID {} not found or update failed.", id);
                throw new ApiException(HttpStatus.NOT_FOUND.getCode(), "Clinic not found or update failed");
            }
        } catch (JpaException e) {
            logger.error("JPA Exception while updating clinic: {}", e.getMessage());
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR.getCode(), "Error updating clinic in the database");
        } catch (Exception e) {
            logger.error("Unexpected error occurred while updating clinic: {}", e.getMessage());
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR.getCode(), "An unexpected error occurred on the server");
        }
    }

    @Override
    public void delete(@NotNull Context ctx) {
        try {
            Long id = ctx.pathParamAsClass("id", Long.class)  // Changed to Long
                    .check(this::validatePrimaryKey, "Not a valid id")
                    .get();
            clinicDAO.delete(id);
            ctx.status(HttpStatus.NO_CONTENT);  // 204 No Content
            logger.info("Clinic with ID {} successfully deleted.", id);
        } catch (JpaException e) {
            logger.error("JPA Exception while deleting clinic: {}", e.getMessage());
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR.getCode(), "Error deleting clinic from the database");
        } catch (Exception e) {
            logger.error("Unexpected error occurred while deleting clinic: {}", e.getMessage());
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR.getCode(), "An unexpected error occurred on the server");
        }
    }

    @Override
    public boolean validatePrimaryKey(Long id) {  // Changed to Long
        try {
            boolean isValid = clinicDAO.validatePrimaryKey(id);
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
    public ClinicDTO validateEntity(@NotNull Context ctx) {
        try {
            ClinicDTO clinicDTO = ctx.bodyValidator(ClinicDTO.class)
                    .check(c -> c.getName() != null && !c.getName().isEmpty(), "Clinic name must be set")
                    .check(c -> c.getAddress() != null && !c.getAddress().isEmpty(), "Clinic address must be set")
                    .get();
            return clinicDTO;
        } catch (Exception e) {
            logger.error("Invalid or missing parameters in the clinic entity: {}", e.getMessage());
            throw new ApiException(HttpStatus.BAD_REQUEST.getCode(), "Invalid or missing parameters in the clinic entity");
        }
    }

    // Fetch opening hours for a specific clinic
    public void getOpeningHours(Context ctx) {
        long clinicId = ctx.pathParamAsClass("id", Long.class).get();
        ClinicDTO clinic = clinicDAO.read(clinicId);

        if (clinic == null) {
            ctx.status(404).json("Clinic not found");
            return;
        }

        // Assuming ClinicDTO has a method to retrieve opening hours
        ctx.json(clinic.getOpeningHours());
    }


    // Fetch all clinics and filter by city using Java Streams
    public void getClinicsByCity(Context ctx) {
        long cityId = ctx.queryParamAsClass("cityId", Long.class).get(); // Get city ID from query parameter

        // Retrieve all clinics and filter by city using streams
        List<ClinicDTO> filteredClinics = clinicDAO.readAll().stream()
                .filter(clinicDTO -> clinicDTO.getCityId() == cityId)  // Filter clinics by cityId
                .collect(Collectors.toList());  // Collect the results to a list

        if (filteredClinics.isEmpty()) {
            ctx.status(404).json("No clinics found for the given city.");
        } else {
            ctx.status(200).json(filteredClinics);
        }
    }

    public void addOpeningHours(@NotNull Context context) {
    }

    public void getClinicsByName(@NotNull Context context) {
    }

    public void addVeterinarianToClinic(@NotNull Context context) {
    }

    public void getVeterinariansInClinic(@NotNull Context context) {
    }

    public void removeVeterinarianFromClinic(@NotNull Context context) {
    }

    
    
    
    
   /* // Get total number of appointments for a specific clinic using streams
    public void getAppointmentCount(Context ctx) {
        long clinicId = ctx.pathParamAsClass("id", Long.class).get();

        // Fetch all appointments for this clinic
        List<Appointment> appointments = appointmentDAO.findByClinicId(clinicId);

        // Use stream to count appointments
        long appointmentCount = appointments.stream().count();

        // Respond with the total count
        ctx.json("Total appointments: " + appointmentCount);
        ctx.status(200);
    }*/
}