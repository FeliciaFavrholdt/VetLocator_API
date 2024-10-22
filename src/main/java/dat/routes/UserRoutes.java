package dat.routes;

import io.javalin.apibuilder.EndpointGroup;

public class UserRoutes {

    private final UserController userController = new UserController();

    protected EndpointGroup getRoutes() {
        return () -> {
            get("/", userController::readAll);             // Retrieve all users
            get("/{id}", userController::read);            // Retrieve specific user by id
            get("/{id}/animals", userController::readAnimals); // Retrieve animals of a user
            post("/", userController::create);             // Create a new user
            put("/{id}", userController::update);          // Update user details
            delete("/{id}", userController::delete);       // Delete specific user
        };
    }
}