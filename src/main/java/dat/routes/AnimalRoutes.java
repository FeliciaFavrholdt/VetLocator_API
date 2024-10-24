package dat.routes;

import dat.controllers.impl.AnimalController;
import dat.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;
import static io.javalin.apibuilder.ApiBuilder.*;

public class AnimalRoutes {

    private final AnimalController animalController = new AnimalController();

    protected EndpointGroup getRoutes() {
        return () -> {
            post("/", animalController::create, Role.USER);          // Create a new animal
            get("/", animalController::readAll, Role.ANYONE);          // Retrieve all animals
            get("/{id}", animalController::read, Role.ANYONE);         // Retrieve specific animal by id
            put("/{id}", animalController::update, Role.USER);       // Update animal details
            delete("/{id}", animalController::delete, Role.USER);    // Delete specific animal
        };
    }
}
