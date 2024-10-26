package dat.controllers.impl;

import dat.config.HibernateConfig;
import dat.controllers.IController;
import dat.dao.impl.ClientDAO;  // Use ClientDAO, not UserDAO
import dat.dto.ClientDTO;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ClientController implements IController<ClientDTO, Integer> {

    private final ClientDAO dao;  // Change from UserDAO to ClientDAO

    public ClientController() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        this.dao = ClientDAO.getInstance(emf);  // Ensure ClientDAO is used here
    }

    @Override
    public void read(@NotNull Context ctx) {
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
        ClientDTO clientDTO = dao.read(id);
        if (clientDTO != null) {
            ctx.res().setStatus(200);
            ctx.json(clientDTO, ClientDTO.class);
        } else {
            ctx.res().setStatus(404);
            ctx.result("Client not found");
        }
    }

    @Override
    public void readAll(@NotNull Context ctx) {
        List<ClientDTO> clientDTOS = dao.readAll();  // Fetch all clients
        ctx.json(clientDTOS);  // Return the list of clients as JSON
    }

    @Override
    public void create(@NotNull Context ctx) {
        ClientDTO jsonRequest = ctx.bodyAsClass(ClientDTO.class);
        ClientDTO clientDTO = dao.create(jsonRequest);
        ctx.res().setStatus(201);
        ctx.json(clientDTO, ClientDTO.class);
    }

    @Override
    public void update(@NotNull Context ctx) {
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
        ClientDTO clientDTO = dao.update(id, validateEntity(ctx));
        if (clientDTO != null) {
            ctx.res().setStatus(200);
            ctx.json(clientDTO, ClientDTO.class);
        } else {
            ctx.res().setStatus(404);
            ctx.result("Client not found or update failed");
        }
    }

    @Override
    public void delete(@NotNull Context ctx) {
        int id = ctx.pathParamAsClass("id", Integer.class)
                .check(this::validatePrimaryKey, "Not a valid id")
                .get();

        try {
            dao.delete(id);
            ctx.status(204);  // No content
        } catch (IllegalArgumentException e) {
            ctx.status(404).json("Client with id: " + id + " not found.");
        } catch (Exception e) {
            ctx.status(500).json("Failed to delete Client with id: " + id + ". Error: " + e.getMessage());
        }
    }




    @Override
    public boolean validatePrimaryKey(Integer id) {
        return dao.validatePrimaryKey(id);
    }

    @Override
    public ClientDTO validateEntity(@NotNull Context ctx) {
        return ctx.bodyValidator(ClientDTO.class)
                .check(u -> u.getFullName() != null && !u.getFullName().isEmpty(), "User full name must be set")
                .check(u -> u.getEmail() != null && !u.getEmail().isEmpty(), "User email must be set")
                .check(u -> u.getPhone() != null, "User phone must be set")
                .get();
    }
}
