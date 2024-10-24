package dat.controllers.impl;

import dat.config.HibernateConfig;
import dat.controllers.IController;
import dat.dao.impl.AnimalDAO;
import dat.dto.AnimalDTO;
import io.javalin.http.Context;
import io.javalin.http.HttpResponseException;
import jakarta.persistence.EntityManagerFactory;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class AnimalController implements IController<AnimalDTO, Integer> {

    private final AnimalDAO dao;

    public AnimalController() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        this.dao = AnimalDAO.getInstance(emf);
    }

    @Override
    public void read(@NotNull Context ctx) {
        try {
            int id = ctx.pathParamAsClass("id", Integer.class)
                    .check(this::validatePrimaryKey, "Invalid animal ID format").get();
            AnimalDTO animalDTO = dao.read(id);
            if (animalDTO != null) {
                ctx.status(200);
                ctx.json(animalDTO);
            } else {
                ctx.status(404).json(Map.of("error", "Animal not found"));
            }
        } catch (Exception e) {
            ctx.status(500).json(Map.of("error", "An unexpected error occurred"));
        }
    }

    @Override
    public void readAll(@NotNull Context ctx) {
        try {
            List<AnimalDTO> animalDTOS = dao.readAll();
            ctx.status(200).json(animalDTOS);
        } catch (Exception e) {
            ctx.status(500).json(Map.of("error", "An unexpected error occurred while retrieving animals"));
        }
    }

    @Override
    public void create(@NotNull Context ctx) {
        try {
            AnimalDTO jsonRequest = ctx.bodyValidator(AnimalDTO.class)
                    .check(a -> a.getName() != null && !a.getName().isEmpty(), "Animal name must be provided")
                    .check(a -> a.getSpecies() != null && !a.getSpecies().isEmpty(), "Animal species must be provided")
                    .check(a -> a.getAge() >= 0, "Animal age must be a non-negative number")
                    .check(a -> a.getUserId() != null, "User ID must be provided")
                    .get();
            AnimalDTO animalDTO = dao.create(jsonRequest);
            ctx.status(201).json(animalDTO);
        } catch (HttpResponseException e) {
            ctx.status(400).json(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            ctx.status(500).json(Map.of("error", "An unexpected error occurred while creating the animal"));
        }
    }

    @Override
    public void update(@NotNull Context ctx) {
        try {
            int id = ctx.pathParamAsClass("id", Integer.class)
                    .check(this::validatePrimaryKey, "Invalid animal ID format").get();
            AnimalDTO animalDTO = dao.update(id, validateEntity(ctx));
            if (animalDTO != null) {
                ctx.status(200).json(animalDTO);
            } else {
                ctx.status(404).json(Map.of("error", "Animal not found or update failed"));
            }
        } catch (HttpResponseException e) {
            ctx.status(400).json(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            ctx.status(500).json(Map.of("error", "An unexpected error occurred while updating the animal"));
        }
    }

    @Override
    public void delete(@NotNull Context ctx) {
        try {
            int id = ctx.pathParamAsClass("id", Integer.class)
                    .check(this::validatePrimaryKey, "Invalid animal ID format").get();
            boolean deleted = dao.delete(id);
            if (deleted) {
                ctx.status(204); // No content, successful deletion
            } else {
                ctx.status(404).json(Map.of("error", "Animal not found"));
            }
        } catch (Exception e) {
            ctx.status(500).json(Map.of("error", "An unexpected error occurred while deleting the animal"));
        }
    }

    @Override
    public boolean validatePrimaryKey(Integer id) {
        return id != null && id > 0 && dao.validatePrimaryKey(id);
    }

    @Override
    public AnimalDTO validateEntity(@NotNull Context ctx) {
        return ctx.bodyValidator(AnimalDTO.class)
                .check(a -> a.getName() != null && !a.getName().isEmpty(), "Animal name must be set")
                .check(a -> a.getSpecies() != null && !a.getSpecies().isEmpty(), "Animal species must be set")
                .check(a -> a.getAge() >= 0, "Animal age must be a non-negative number")
                .check(a -> a.getUserId() != null, "Owner (user) ID must be set")
                .get();
    }

}
