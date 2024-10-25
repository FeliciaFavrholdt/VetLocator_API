package dat.routes;

import dat.controllers.impl.ClientController;
import dat.security.controllers.SecurityController;
import dat.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;
import static io.javalin.apibuilder.ApiBuilder.*;

public class ClientRoutes {

    private final ClientController clientController = new ClientController();
    private static SecurityController securityController = SecurityController.getInstance();

    protected EndpointGroup getRoutes() {
        return () -> {
            // Retrieve all users
            get("/api/users", clientController::readAll, Role.ADMIN);

            // Retrieve a specific user by ID
            get("/api/users/:id", clientController::read, Role.ADMIN);

            // Retrieve animals of a specific user by ID
            get("/api/users/:id/animals", clientController::getAnimals, Role.ADMIN);

            // Create a new user
            post("/api/users", clientController::create, Role.ADMIN);

            // Update a specific user by ID
            put("/api/users/:id", clientController::update, Role.ADMIN);

            // Delete a specific user by ID
            delete("/api/users/:id", clientController::delete, Role.ADMIN);
        };
    }
}