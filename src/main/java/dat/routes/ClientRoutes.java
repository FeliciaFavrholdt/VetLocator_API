package dat.routes;

import dat.controllers.impl.ClientController;
import io.javalin.apibuilder.EndpointGroup;
import static io.javalin.apibuilder.ApiBuilder.*;

public class ClientRoutes {

    private final ClientController clientController = new ClientController();

    protected EndpointGroup getRoutes() {
        return () -> {
            // Retrieve all users
            get("/api/users", clientController::readAll);

            // Retrieve a specific user by ID
            get("/api/users/:id", clientController::read);

            // Retrieve animals of a specific user by ID
            get("/api/users/:id/animals", clientController::getAnimals);

            // Create a new user
            post("/api/users", clientController::create);

            // Update a specific user by ID
            put("/api/users/:id", clientController::update);

            // Delete a specific user by ID
            delete("/api/users/:id", clientController::delete);
        };
    }
}