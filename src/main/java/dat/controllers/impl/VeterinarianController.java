package dat.controllers.impl;

import dat.config.HibernateConfig;
import dat.controllers.IController;
import dat.dao.impl.VeterinarianDAO;
import dat.dto.VeterinarianDTO;
import dat.exceptions.ApiException;
import dat.exceptions.JpaException;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class VeterinarianController implements IController<VeterinarianDTO, Integer> {

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
                ctx.status(200);
                ctx.json(veterinarianDTO);
            } else {
                throw new ApiException(404, "Veterinarian not found");
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
            List<VeterinarianDTO> veterinarianDTOS = dao.readAll();
            ctx.status(200);
            ctx.json(veterinarianDTOS);
        } catch (JpaException e) {
            throw new ApiException(500, "Error fetching veterinarians from database");
        } catch (Exception e) {
            throw new ApiException(500, "An unexpected error occurred on the server");
        }
    }

    @Override
    public void create(@NotNull Context ctx) {
        try {
            VeterinarianDTO jsonRequest = ctx.bodyAsClass(VeterinarianDTO.class);
            VeterinarianDTO veterinarianDTO = dao.create(jsonRequest);
            ctx.status(201);
            ctx.json(veterinarianDTO);
        } catch (JpaException e) {
            throw new ApiException(500, "Error creating veterinarian in the database");
        } catch (Exception e) {
            throw new ApiException(500, "An unexpected error occurred");
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
                ctx.status(200);
                ctx.json(veterinarianDTO);
            } else {
                throw new ApiException(404, "Veterinarian not found or update failed");
            }
        } catch (JpaException e) {
            throw new ApiException(500, "Error updating veterinarian in the database");
        } catch (Exception e) {
            throw new ApiException(500, "An unexpected error occurred on the server");
        }
    }

    @Override
    public void delete(@NotNull Context ctx) {
        try {
            int id = ctx.pathParamAsClass("id", Integer.class)
                    .check(this::validatePrimaryKey, "Not a valid id")
                    .get();
            dao.delete(id);
            ctx.status(204);
        } catch (JpaException e) {
            throw new ApiException(500, "Error deleting veterinarian from the database");
        } catch (Exception e) {
            throw new ApiException(500, "An unexpected error occurred on the server");
        }
    }

    @Override
    public boolean validatePrimaryKey(Integer id) {
        try {
            return dao.validatePrimaryKey(id);
        } catch (JpaException e) {
            throw new ApiException(500, "Database error during primary key validation");
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
            throw new ApiException(400, "Invalid or missing parameters in the veterinarian entity");
        }
    }
}