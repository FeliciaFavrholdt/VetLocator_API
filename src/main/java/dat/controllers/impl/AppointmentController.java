package dat.controllers.impl;

import dat.config.HibernateConfig;
import dat.controllers.IController;
import dat.dao.impl.AppointmentDAO;
import dat.dto.AppointmentDTO;
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
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
        AppointmentDTO appointmentDTO = dao.read(id);
        ctx.res().setStatus(200);
        ctx.json(appointmentDTO, AppointmentDTO.class);
    }

    @Override
    public void readAll(@NotNull Context ctx) {
        List<AppointmentDTO> appointmentDTOS = dao.readAll();
        ctx.res().setStatus(200);
        ctx.json(appointmentDTOS, AppointmentDTO.class);
    }

    @Override
    public void create(@NotNull Context ctx) {
        AppointmentDTO jsonRequest = ctx.bodyAsClass(AppointmentDTO.class);
        AppointmentDTO appointmentDTO = dao.create(jsonRequest);
        ctx.res().setStatus(201);
        ctx.json(appointmentDTO, AppointmentDTO.class);
    }

    @Override
    public void update(@NotNull Context ctx) {
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
        AppointmentDTO appointmentDTO = dao.update(id, validateEntity(ctx));
        ctx.res().setStatus(200);
        ctx.json(appointmentDTO, AppointmentDTO.class);
    }

    @Override
    public void delete(@NotNull Context ctx) {
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
        dao.delete(id);
        ctx.res().setStatus(204);
    }

    @Override
    public boolean validatePrimaryKey(Integer id) {
        return dao.validatePrimaryKey(id);
    }

    @Override
    public AppointmentDTO validateEntity(@NotNull Context ctx) {
        return ctx.bodyValidator(AppointmentDTO.class)
                .check(a -> a.getDate() != null, "Appointment date must be set")
                .check(a -> a.getTime() != null, "Appointment time must be set")
                .check(a -> a.getReason() != null && !a.getReason().isEmpty(), "Reason for appointment must be set")
                .check(a -> a.getVeterinarianId() != null, "Veterinarian (Clinic) ID must be associated with the appointment")
                .check(a -> a.getClientId() != null, "Client ID must be associated with the appointment")
                .check(a -> a.getAnimalId() != null, "Animal ID must be associated with the appointment")
                .get();
    }
}
