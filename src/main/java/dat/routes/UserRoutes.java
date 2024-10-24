package dat.routes;

import dat.controllers.impl.ClientController;
import io.javalin.apibuilder.EndpointGroup;
import static io.javalin.apibuilder.ApiBuilder.*;

public class UserRoutes {

    private final ClientController clientController = new ClientController();

    protected EndpointGroup getRoutes() {
        return () -> {
            get("/", clientController::readAll);             // Retrieve all users
            get("/{id}", clientController::read);            // Retrieve specific user by id
            get("/{id}/animals", clientController::readUserAnimals);
            post("/", clientController::create);             // Create a new user
            put("/{id}", clientController::update);          // Update user details
            delete("/{id}", clientController::delete);       // Delete specific user
        };
    }
}