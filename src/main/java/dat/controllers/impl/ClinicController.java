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

import java.util.List;

public class ClinicController implements IController<ClinicDTO, Long> {  // Primary key type changed to Long

    private final ClinicDAO dao;

    public ClinicController() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        this.dao = ClinicDAO.getInstance(emf);
    }

    @Override
    public void read(@NotNull Context ctx) {
        try {
            Long id = ctx.pathParamAsClass("id", Long.class)  // Changed to Long
                    .check(this::validatePrimaryKey, "Not a valid id")
                    .get();
            ClinicDTO clinicDTO = dao.read(id);

            if (clinicDTO != null) {
                ctx.status(200);
                ctx.json(clinicDTO);
            } else {
                throw new ApiException(404, "Clinic not found");
            }
        } catch (ApiException e) {
            ctx.status(e.getStatusCode());
            ctx.json(e.getMessageRecord());
        } catch (JpaException e) {
            ctx.status(e.getStatusCode());
            ctx.json(e.getMessageRecord());
        } catch (Exception e) {
            throw new ApiException(500, "Internal Server Error");
        }
    }

    @Override
    public void readAll(@NotNull Context ctx) {
        try {
            List<ClinicDTO> clinicDTOS = dao.readAll();
            ctx.status(200);
            ctx.json(clinicDTOS);
        } catch (JpaException e) {
            throw new ApiException(500, "Error fetching clinics from database");
        } catch (Exception e) {
            throw new ApiException(500, "An unexpected error occurred on the server");
        }
    }

    @Override
    public void create(@NotNull Context ctx) {
        try {
            ClinicDTO jsonRequest = ctx.bodyAsClass(ClinicDTO.class);
            ClinicDTO clinicDTO = dao.create(jsonRequest);
            ctx.status(201);
            ctx.json(clinicDTO);
        } catch (JpaException e) {
            throw new ApiException(500, "Error creating clinic in the database");
        } catch (Exception e) {
            throw new ApiException(500, "An unexpected error occurred");
        }
    }

    @Override
    public void update(@NotNull Context ctx) {
        try {
            Long id = ctx.pathParamAsClass("id", Long.class)  // Changed to Long
                    .check(this::validatePrimaryKey, "Not a valid id")
                    .get();
            ClinicDTO clinicDTO = dao.update(id, validateEntity(ctx));

            if (clinicDTO != null) {
                ctx.status(200);
                ctx.json(clinicDTO);
            } else {
                throw new ApiException(404, "Clinic not found or update failed");
            }
        } catch (JpaException e) {
            throw new ApiException(500, "Error updating clinic in the database");
        } catch (Exception e) {
            throw new ApiException(500, "An unexpected error occurred on the server");
        }
    }

    @Override
    public void delete(@NotNull Context ctx) {
        try {
            Long id = ctx.pathParamAsClass("id", Long.class)  // Changed to Long
                    .check(this::validatePrimaryKey, "Not a valid id")
                    .get();
            dao.delete(id);
            ctx.status(204);
        } catch (JpaException e) {
            throw new ApiException(500, "Error deleting clinic from the database");
        } catch (Exception e) {
            throw new ApiException(500, "An unexpected error occurred on the server");
        }
    }

    @Override
    public boolean validatePrimaryKey(Long id) {  // Changed to Long
        try {
            return dao.validatePrimaryKey(id);
        } catch (JpaException e) {
            throw new ApiException(500, "Database error during primary key validation");
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
            throw new ApiException(400, "Invalid or missing parameters in the clinic entity");
        }
    }
}
