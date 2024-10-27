package dat.controller.impl;

import dat.config.HibernateConfig;
import dat.controller.IController;
import dat.dao.impl.ClientDAO;  // Use ClientDAO, not UserDAO
import dat.dto.ClientDTO;
import dat.exceptions.JpaException;
import dat.security.exceptions.ApiException;
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
        this.dao = ClientDAO.getInstance(emf);  // Ensure ClientDAO is used here
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
                ctx.status(200);
                ctx.json(clientDTO);
                logger.info("Client with ID: {} found and returned", id);
            } else {
                throw new ApiException(404, "Client not found");
            }
        } catch (ApiException e) {
            logger.warn("API Error: {}", e.getMessage(), e);
            ctx.status(e.getStatusCode());
            ctx.json(e.getMessageRecord());
        } catch (JpaException e) {
            logger.error("JPA Error: {}", e.getMessage(), e);
            ctx.status(e.getStatusCode());
            ctx.json(e.getMessageRecord());
        } catch (Exception e) {
            logger.error("General Error: {}", e.getMessage(), e);
            throw new ApiException(500, "An unexpected error occurred on the server");
        }
    }

    @Override
    public void readAll(@NotNull Context ctx) {
        try {
            logger.info("Fetching all clients");
            List<ClientDTO> clientDTOS = dao.readAll();
            ctx.status(200);
            ctx.json(clientDTOS);
            logger.info("All clients fetched successfully, count: {}", clientDTOS.size());
        } catch (JpaException e) {
            logger.error("JPA Error fetching clients: {}", e.getMessage(), e);
            throw new ApiException(500, "Error fetching clients from database");
        } catch (Exception e) {
            logger.error("Error fetching all clients: {}", e.getMessage(), e);
            throw new ApiException(500, "An unexpected error occurred on the server");
        }
    }

    @Override
    public void create(@NotNull Context ctx) {
        try {
            ClientDTO jsonRequest = ctx.bodyAsClass(ClientDTO.class);
            logger.info("Creating new client: {}", jsonRequest);
            ClientDTO clientDTO = dao.create(jsonRequest);
            ctx.status(201);
            ctx.json(clientDTO);
            logger.info("Client created successfully with ID: {}", clientDTO.getId());
        } catch (JpaException e) {
            logger.error("JPA Error creating client: {}", e.getMessage(), e);
            throw new ApiException(500, "Error creating client in the database");
        } catch (Exception e) {
            logger.error("Error creating client: {}", e.getMessage(), e);
            throw new ApiException(500, "An unexpected error occurred on the server");
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
                ctx.status(200);
                ctx.json(clientDTO);
                logger.info("Client with ID: {} updated successfully", id);
            } else {
                throw new ApiException(404, "Client not found or update failed");
            }
        } catch (JpaException e) {
            logger.error("JPA Error updating client: {}", e.getMessage(), e);
            throw new ApiException(500, "Error updating client in the database");
        } catch (Exception e) {
            logger.error("Error updating client: {}", e.getMessage(), e);
            throw new ApiException(500, "An unexpected error occurred on the server");
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
            ctx.status(204);
            logger.info("Client with ID: {} deleted successfully", id);
        } catch (JpaException e) {
            logger.error("JPA Error deleting client: {}", e.getMessage(), e);
            throw new ApiException(500, "Error deleting client from the database");
        } catch (Exception e) {
            logger.error("Error deleting client: {}", e.getMessage(), e);
            throw new ApiException(500, "An unexpected error occurred on the server");
        }
    }

    @Override
    public boolean validatePrimaryKey(Integer id) {
        try {
            boolean isValid = dao.validatePrimaryKey(id);
            if (!isValid) {
                throw new ApiException(400, "Invalid primary key: " + id);
            }
            return isValid;
        } catch (JpaException e) {
            logger.error("JPA Error validating primary key: {}", e.getMessage(), e);
            throw new ApiException(500, "Database error during primary key validation");
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
            throw new ApiException(400, "Invalid client entity");
        }
    }
}
