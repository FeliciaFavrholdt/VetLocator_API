package dat.controllers.impl;

import dat.config.HibernateConfig;
import dat.controllers.IController;
import dat.dao.impl.ClinicDAO;
import dat.dto.ClinicDTO;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Controller class for Clinic entity.
 *
 * This class includes validation logic for both primary keys and entity data using bodyValidator and pathParamAsClass.
 * It handles the HTTP response by setting appropriate status codes and returning the result in JSON format.
 */

public class ClinicController implements IController<ClinicDTO, Integer> {

    private final ClinicDAO dao;

    public ClinicController() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        this.dao = ClinicDAO.getInstance(emf);
    }

    @Override
    public void read(@NotNull Context ctx) {
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
        ClinicDTO clinicDTO = dao.read(id);
        if (clinicDTO != null) {
            ctx.res().setStatus(200);
            ctx.json(clinicDTO, ClinicDTO.class);
        } else {
           // ctx.res().setStatus(404).json("{ \"message\": \"Clinic not found\" }");
        }
    }

    @Override
    public void readAll(@NotNull Context ctx) {
        List<ClinicDTO> clinicDTOS = dao.readAll();
        ctx.res().setStatus(200);
        ctx.json(clinicDTOS, ClinicDTO.class);
    }

    @Override
    public void create(@NotNull Context ctx) {
        ClinicDTO jsonRequest = ctx.bodyAsClass(ClinicDTO.class);
        ClinicDTO clinicDTO = dao.create(jsonRequest);
        ctx.res().setStatus(201);
        ctx.json(clinicDTO, ClinicDTO.class);
    }

    @Override
    public void update(@NotNull Context ctx) {
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
        ClinicDTO clinicDTO = dao.update(id, validateEntity(ctx));
        if (clinicDTO != null) {
            ctx.res().setStatus(200);
            ctx.json(clinicDTO, ClinicDTO.class);
        } else {
            //ctx.res().setStatus(404).json("{ \"message\": \"Clinic not found or update failed\" }");
        }
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
    public ClinicDTO validateEntity(@NotNull Context ctx) {
        return ctx.bodyValidator(ClinicDTO.class)
                .check(c -> c.getClinicName() != null && !c.getClinicName().isEmpty(), "Clinic name must be set")
                .check(c -> c.getSpecialization() != null, "Clinic specialization must be set")
                .check(c -> c.getPhone() != null && !c.getPhone().isEmpty(), "Phone number must be set")
                .check(c -> c.getEmail() != null && !c.getEmail().isEmpty(), "Email must be set")
                .check(c -> c.getAddress() != null && !c.getAddress().isEmpty(), "Clinic address must be set")
                .check(c -> c.getCityName() != null && !c.getCityName().isEmpty(), "City name must be set")
                .check(c -> c.getPostalCode() != null && !c.getPostalCode().isEmpty(), "Postal code must be set")
                .get();
    }
}