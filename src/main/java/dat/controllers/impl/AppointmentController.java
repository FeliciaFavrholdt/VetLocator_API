package dat.controllers.impl;

import dat.config.HibernateConfig;
import dat.controllers.IController;
import dat.dao.impl.AppointmentDAO;
import dat.dto.AppointmentDTO;
import dat.exceptions.ApiException;
import dat.exceptions.JpaException;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AppointmentController implements IController<AppointmentDTO, Integer> {

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
                ctx.status(200);
                ctx.json(appointmentDTO);
            } else {
                throw new ApiException(404, "Appointment not found");
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
            List<AppointmentDTO> appointmentDTOS = dao.readAll();
            ctx.status(200);
            ctx.json(appointmentDTOS);
        } catch (JpaException e) {
            throw new ApiException(500, "Error fetching appointments from database");
        } catch (Exception e) {
            throw new ApiException(500, "An unexpected error occurred on the server");
        }
    }

    @Override
    public void create(@NotNull Context ctx) {
        try {
            AppointmentDTO jsonRequest = ctx.bodyAsClass(AppointmentDTO.class);
            AppointmentDTO appointmentDTO = dao.create(jsonRequest);
            ctx.status(201);
            ctx.json(appointmentDTO);
        } catch (JpaException e) {
            throw new ApiException(500, "Error creating appointment in the database");
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
            AppointmentDTO appointmentDTO = dao.update(id, validateEntity(ctx));

            if (appointmentDTO != null) {
                ctx.status(200);
                ctx.json(appointmentDTO);
            } else {
                throw new ApiException(404, "Appointment not found or update failed");
            }
        } catch (JpaException e) {
            throw new ApiException(500, "Error updating appointment in the database");
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
            throw new ApiException(500, "Error deleting appointment from the database");
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
    public AppointmentDTO validateEntity(@NotNull Context ctx) {
        try {
            AppointmentDTO appointmentDTO = ctx.bodyValidator(AppointmentDTO.class)
                    .check(a -> a.getDate() != null, "Appointment date must be set")
                    .check(a -> a.getVeterinarianId() != null, "Veterinarian ID must be set")
                    .check(a -> a.getAnimalId() != null, "Animal ID must be set")
                    .get();
            return appointmentDTO;
        } catch (Exception e) {
            throw new ApiException(400, "Invalid or missing parameters in the appointment entity");
        }
    }
}
