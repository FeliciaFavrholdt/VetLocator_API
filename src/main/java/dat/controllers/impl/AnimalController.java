package dat.controllers.impl;

import dat.config.HibernateConfig;
import dat.controllers.IController;
import dat.dao.impl.AnimalDAO;
import dat.dto.AnimalDTO;
import dat.exceptions.ApiException;
import dat.exceptions.JpaException;
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
        try {
            int id = ctx.pathParamAsClass("id", Integer.class)
                    .check(this::validatePrimaryKey, "Not a valid id")
                    .get();
            AnimalDTO animalDTO = dao.read(id);

            if (animalDTO != null) {
                ctx.status(200);
                ctx.json(animalDTO);
            } else {
                throw new ApiException(404, "Animal not found");
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
            List<AnimalDTO> animalDTOS = dao.readAll();
            ctx.status(200);
            ctx.json(animalDTOS);
        } catch (JpaException e) {
            throw new ApiException(500, "Error fetching animals from database");
        } catch (Exception e) {
            throw new ApiException(500, "An unexpected error occurred on the server");
        }
    }

    @Override
    public void create(@NotNull Context ctx) {
        try {
            AnimalDTO jsonRequest = ctx.bodyAsClass(AnimalDTO.class);
            AnimalDTO animalDTO = dao.create(jsonRequest);
            ctx.status(201);
            ctx.json(animalDTO);
        } catch (JpaException e) {
            throw new ApiException(500, "Error creating animal in the database");
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
            AnimalDTO animalDTO = dao.update(id, validateEntity(ctx));

            if (animalDTO != null) {
                ctx.status(200);
                ctx.json(animalDTO);
            } else {
                throw new ApiException(404, "Animal not found or update failed");
            }
        } catch (JpaException e) {
            throw new ApiException(500, "Error updating animal in the database");
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
            throw new ApiException(500, "Error deleting animal from the database");
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
    public AnimalDTO validateEntity(@NotNull Context ctx) {
        try {
            AnimalDTO animalDTO = ctx.bodyValidator(AnimalDTO.class)
                    .check(a -> a.getName() != null && !a.getName().isEmpty(), "Animal name must be set")
                    .check(a -> a.getSpecies() != null && !a.getSpecies().isEmpty(), "Animal species must be set")
                    .check(a -> a.getAge() >= 0, "Animal age must be a non-negative number")
                    .check(a -> a.getOwnerId() != null, "Owner (user) ID must be set")
                    .get();
            return animalDTO;
        } catch (Exception e) {
            throw new ApiException(400, "Invalid or missing parameters in the animal entity");
        }
    }
}

