package dat.controllers.impl;

import dat.config.HibernateConfig;
import dat.controllers.IController;
import dat.dao.impl.ClientDAO;
import dat.dto.ClientDTO;
import dat.exceptions.ApiException;
import dat.exceptions.JpaException;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ClientController implements IController<ClientDTO, Integer> {

    private final ClientDAO dao;

    public ClientController() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        this.dao = ClientDAO.getInstance(emf);
    }

    @Override
    public void read(@NotNull Context ctx) {
        try {
            int id = ctx.pathParamAsClass("id", Integer.class)
                    .check(this::validatePrimaryKey, "Not a valid id")
                    .get();
            ClientDTO clientDTO = dao.read(id);

            if (clientDTO != null) {
                ctx.status(200);
                ctx.json(clientDTO);
            } else {
                throw new ApiException(404, "Client not found");
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
            List<ClientDTO> clientDTOS = dao.readAll();
            ctx.status(200);
            ctx.json(clientDTOS);
        } catch (JpaException e) {
            throw new ApiException(500, "Error fetching clients from database");
        } catch (Exception e) {
            throw new ApiException(500, "An unexpected error occurred on the server");
        }
    }

    @Override
    public void create(@NotNull Context ctx) {
        try {
            ClientDTO jsonRequest = ctx.bodyAsClass(ClientDTO.class);
            ClientDTO clientDTO = dao.create(jsonRequest);
            ctx.status(201);
            ctx.json(clientDTO);
        } catch (JpaException e) {
            throw new ApiException(500, "Error creating client in the database");
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
            ClientDTO clientDTO = dao.update(id, validateEntity(ctx));

            if (clientDTO != null) {
                ctx.status(200);
                ctx.json(clientDTO);
            } else {
                throw new ApiException(404, "Client not found or update failed");
            }
        } catch (JpaException e) {
            throw new ApiException(500, "Error updating client in the database");
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
            throw new ApiException(500, "Error deleting client from the database");
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
    public ClientDTO validateEntity(@NotNull Context ctx) {
        try {
            ClientDTO clientDTO = ctx.bodyValidator(ClientDTO.class)
                    .check(c -> c.getName() != null && !c.getName().isEmpty(), "Client name must be set")
                    .check(c -> c.getEmail() != null && !c.getEmail().isEmpty(), "Client email must be set")
                    .get();
            return clientDTO;
        } catch (Exception e) {
            throw new ApiException(400, "Invalid or missing parameters in the client entity");
        }
    }
}