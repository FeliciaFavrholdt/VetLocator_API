package dat.controllers.impl;

import dat.config.HibernateConfig;
import dat.controllers.IController;
import dat.dao.impl.AnimalDAO;
import dat.dto.AnimalDTO;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AnimalController implements IController<AnimalDTO, Integer> {

    private final AnimalDAO dao;

    public AnimalController() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        this.dao = AnimalDAO.getInstance(emf);
    }

    @Override
    public void read(@NotNull Context ctx) {
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
        AnimalDTO animalDTO = dao.read(id);
        ctx.res().setStatus(200);
        ctx.json(animalDTO, AnimalDTO.class);
    }

    @Override
    public void readAll(@NotNull Context ctx) {
        List<AnimalDTO> animalDTOS = dao.readAll();
        ctx.res().setStatus(200);
        ctx.json(animalDTOS, AnimalDTO.class);
    }

    @Override
    public void create(@NotNull Context ctx) {
        AnimalDTO jsonRequest = ctx.bodyAsClass(AnimalDTO.class);
        AnimalDTO animalDTO = dao.create(jsonRequest);
        ctx.res().setStatus(201);
        ctx.json(animalDTO, AnimalDTO.class);
    }

    @Override
    public void update(@NotNull Context ctx) {
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
        AnimalDTO animalDTO = dao.update(id, validateEntity(ctx));
        ctx.res().setStatus(200);
        ctx.json(animalDTO, AnimalDTO.class);
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
    public AnimalDTO validateEntity(@NotNull Context ctx) {
        return ctx.bodyValidator(AnimalDTO.class)
                .check(a -> a.getName() != null && !a.getName().isEmpty(), "Animal name must be set")
                .check(a -> a.getSpecies() != null && !a.getSpecies().isEmpty(), "Animal species must be set")
                .check(a -> a.getAge() != null, "Animal age must be set")
                .get();
    }
}