package dat.controllers.impl;

import dat.config.HibernateConfig;
import dat.controllers.IController;
import dat.dao.impl.ClientDAO;
import dat.dto.ClientDTO;
import dat.exceptions.ApiException;
import dat.exceptions.JpaException;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import jakarta.persistence.EntityManagerFactory;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ClientController implements IController<ClientDTO, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(ClientController.class);  // Logger instance
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
                ctx.status(HttpStatus.OK);  // 200 OK
                ctx.json(clientDTO);
                logger.info("Client with ID {} successfully retrieved.", id);
            } else {
                logger.warn("Client with ID {} not found.", id);
                throw new ApiException(HttpStatus.NOT_FOUND.getCode(), "Client not found");
            }
        } catch (ApiException e) {
            logger.error("API Exception while fetching client: {}", e.getMessage());
            ctx.status(e.getStatusCode());
            ctx.json(e.getMessageRecord());
        } catch (JpaException e) {
            logger.error("JPA Exception while fetching client: {}", e.getMessage());
            ctx.status(e.getStatusCode());
            ctx.json(e.getMessageRecord());
        } catch (Exception e) {
            logger.error("Unexpected error occurred while fetching client: {}", e.getMessage());
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR.getCode(), "Internal Server Error");
        }
    }

    @Override
    public void readAll(@NotNull Context ctx) {
        try {
            List<ClientDTO> clientDTOS = dao.readAll();
            ctx.status(HttpStatus.OK);  // 200 OK
            ctx.json(clientDTOS);
            logger.info("Successfully fetched all clients.");
        } catch (JpaException e) {
            logger.error("JPA Exception while fetching all clients: {}", e.getMessage());
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR.getCode(), "Error fetching clients from the database");
        } catch (Exception e) {
            logger.error("Unexpected error occurred while fetching clients: {}", e.getMessage());
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR.getCode(), "An unexpected error occurred on the server");
        }
    }

    @Override
    public void create(@NotNull Context ctx) {
        try {
            ClientDTO jsonRequest = ctx.bodyAsClass(ClientDTO.class);
            ClientDTO clientDTO = dao.create(jsonRequest);
            ctx.status(HttpStatus.CREATED);  // 201 Created
            ctx.json(clientDTO);
            logger.info("Successfully created new client with ID {}", clientDTO.getId());
        } catch (JpaException e) {
            logger.error("JPA Exception while creating client: {}", e.getMessage());
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR.getCode(), "Error creating client in the database");
        } catch (Exception e) {
            logger.error("Unexpected error occurred while creating client: {}", e.getMessage());
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR.getCode(), "An unexpected error occurred");
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
                ctx.status(HttpStatus.OK);  // 200 OK
                ctx.json(clientDTO);
                logger.info("Client with ID {} successfully updated.", id);
            } else {
                logger.warn("Client with ID {} not found or update failed.", id);
                throw new ApiException(HttpStatus.NOT_FOUND.getCode(), "Client not found or update failed");
            }
        } catch (JpaException e) {
            logger.error("JPA Exception while updating client: {}", e.getMessage());
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR.getCode(), "Error updating client in the database");
        } catch (Exception e) {
            logger.error("Unexpected error occurred while updating client: {}", e.getMessage());
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR.getCode(), "An unexpected error occurred on the server");
        }
    }

    @Override
    public void delete(@NotNull Context ctx) {
        try {
            int id = ctx.pathParamAsClass("id", Integer.class)
                    .check(this::validatePrimaryKey, "Not a valid id")
                    .get();
            dao.delete(id);
            ctx.status(HttpStatus.NO_CONTENT);  // 204 No Content
            logger.info("Client with ID {} successfully deleted.", id);
        } catch (JpaException e) {
            logger.error("JPA Exception while deleting client: {}", e.getMessage());
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR.getCode(), "Error deleting client from the database");
        } catch (Exception e) {
            logger.error("Unexpected error occurred while deleting client: {}", e.getMessage());
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR.getCode(), "An unexpected error occurred on the server");
        }
    }

    @Override
    public boolean validatePrimaryKey(Integer id) {
        try {
            boolean isValid = dao.validatePrimaryKey(id);
            if (!isValid) {
                logger.warn("Invalid primary key: {}", id);
                throw new ApiException(HttpStatus.BAD_REQUEST.getCode(), "Invalid primary key");
            }
            return isValid;
        } catch (JpaException e) {
            logger.error("JPA Exception during primary key validation: {}", e.getMessage());
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR.getCode(), "Database error during primary key validation");
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
            logger.error("Invalid or missing parameters in the client entity: {}", e.getMessage());
            throw new ApiException(HttpStatus.BAD_REQUEST.getCode(), "Invalid or missing parameters in the client entity");
        }
    }
}