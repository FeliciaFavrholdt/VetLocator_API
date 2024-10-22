package dat.controllers.impl;

import dat.config.HibernateConfig;
import dat.controllers.IController;
import dat.dao.impl.UserDAO;
import dat.dto.UserDTO;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class UserController implements IController<UserDTO, Integer> {

    private final UserDAO dao;

    public UserController() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        this.dao = UserDAO.getInstance(emf);
    }

    @Override
    public void read(@NotNull Context ctx) {
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
        UserDTO userDTO = dao.read(id);
        ctx.res().setStatus(200);
        ctx.json(userDTO, UserDTO.class);
    }

    @Override
    public void readAll(@NotNull Context ctx) {
        List<UserDTO> userDTOS = dao.readAll();
        ctx.res().setStatus(200);
        ctx.json(userDTOS, UserDTO.class);
    }

    @Override
    public void create(@NotNull Context ctx) {
        UserDTO jsonRequest = ctx.bodyAsClass(UserDTO.class);
        UserDTO userDTO = dao.create(jsonRequest);
        ctx.res().setStatus(201);
        ctx.json(userDTO, UserDTO.class);
    }

    @Override
    public void update(@NotNull Context ctx) {
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
        UserDTO userDTO = dao.update(id, validateEntity(ctx));
        ctx.res().setStatus(200);
        ctx.json(userDTO, UserDTO.class);
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
    public UserDTO validateEntity(@NotNull Context ctx) {
        return ctx.bodyValidator(UserDTO.class)
                .check(u -> u.getFullName() != null && !u.getFullName().isEmpty(), "User full name must be set")
                .check(u -> u.getEmail() != null && !u.getEmail().isEmpty(), "User email must be set")
                .check(u -> u.getPhone() != null, "User phone must be set")
                .get();
    }
}