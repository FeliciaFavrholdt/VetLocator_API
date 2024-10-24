package dat.controllers.impl;

import dat.config.HibernateConfig;
import dat.controllers.IController;
import dat.dao.impl.ClientDAO;
import dat.dto.ClientDTO;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ClientController implements IController<ClientDTO, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(ClientController.class);
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
            logger.info("Reading client with ID: {}", id);
            ClientDTO clientDTO = dao.read(id);

            if (clientDTO != null) {
                ctx.res().setStatus(200);
                ctx.json(clientDTO, ClientDTO.class);
                logger.info("Client with ID: {} found and returned", id);
            } else {
                ctx.res().setStatus(404);
                ctx.result("Client not found");
                logger.warn("Client with ID: {} not found", id);
            }
        } catch (Exception e) {
            logger.error("Error reading client with ID: {}", e.getMessage(), e);
            ctx.res().setStatus(500);
            ctx.result("Internal Server Error");
        }
    }

    @Override
    public void readAll(@NotNull Context ctx) {
        try {
            logger.info("Fetching all clients");
            List<ClientDTO> clientDTOS = dao.readAll();
            ctx.res().setStatus(200);
            ctx.json(clientDTOS, ClientDTO.class);
            logger.info("All clients fetched successfully, count: {}", clientDTOS.size());
        } catch (Exception e) {
            logger.error("Error fetching all clients: {}", e.getMessage(), e);
            ctx.res().setStatus(500);
            ctx.result("Internal Server Error");
        }
    }

    @Override
    public void create(@NotNull Context ctx) {
        try {
            ClientDTO jsonRequest = ctx.bodyAsClass(ClientDTO.class);
            logger.info("Creating new client: {}", jsonRequest);
            ClientDTO clientDTO = dao.create(jsonRequest);
            ctx.res().setStatus(201);
            ctx.json(clientDTO, ClientDTO.class);
            logger.info("Client created successfully with ID: {}", clientDTO.getId());
        } catch (Exception e) {
            logger.error("Error creating client: {}", e.getMessage(), e);
            ctx.res().setStatus(500);
            ctx.result("Internal Server Error");
        }
    }

    @Override
    public void update(@NotNull Context ctx) {
        try {
            int id = ctx.pathParamAsClass("id", Integer.class)
                    .check(this::validatePrimaryKey, "Not a valid id")
                    .get();
            logger.info("Updating client with ID: {}", id);
            ClientDTO clientDTO = dao.update(id, validateEntity(ctx));

            if (clientDTO != null) {
                ctx.res().setStatus(200);
                ctx.json(clientDTO, ClientDTO.class);
                logger.info("Client with ID: {} updated successfully", id);
            } else {
                ctx.res().setStatus(404);
                ctx.result("Client not found or update failed");
                logger.warn("Client with ID: {} not found or update failed", id);
            }
        } catch (Exception e) {
            logger.error("Error updating client with ID: {}", e.getMessage(), e);
            ctx.res().setStatus(500);
            ctx.result("Internal Server Error");
        }
    }

    @Override
    public void delete(@NotNull Context ctx) {
        try {
            int id = ctx.pathParamAsClass("id", Integer.class)
                    .check(this::validatePrimaryKey, "Not a valid id")
                    .get();
            logger.info("Deleting client with ID: {}", id);
            dao.delete(id);
            ctx.res().setStatus(204);
            logger.info("Client with ID: {} deleted successfully", id);
        } catch (Exception e) {
            logger.error("Error deleting client with ID: {}", e.getMessage(), e);
            ctx.res().setStatus(500);
            ctx.result("Internal Server Error");
        }
    }

    @Override
    public boolean validatePrimaryKey(Integer id) {
        try {
            boolean isValid = dao.validatePrimaryKey(id);
            if (!isValid) {
                logger.warn("Invalid primary key: {}", id);
            }
            return isValid;
        } catch (Exception e) {
            logger.error("Error validating primary key: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    public ClientDTO validateEntity(@NotNull Context ctx) {
        try {
            ClientDTO clientDTO = ctx.bodyValidator(ClientDTO.class)
                    .check(u -> u.getFullName() != null && !u.getFullName().isEmpty(), "Client full name must be set")
                    .check(u -> u.getEmail() != null && !u.getEmail().isEmpty(), "Client email must be set")
                    .check(u -> u.getPhone() != null, "Client phone must be set")
                    .get();
            logger.info("Client entity validated successfully: {}", clientDTO);
            return clientDTO;
        } catch (Exception e) {
            logger.error("Error validating client entity: {}", e.getMessage(), e);
            throw e;  // Let the exception bubble up to be handled in the calling method.
        }
    }
}