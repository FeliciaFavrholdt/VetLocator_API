package dat.routes;

import dat.controllers.impl.AnimalController;
import dat.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;
import static io.javalin.apibuilder.ApiBuilder.*;

public class AnimalRoutes {

    private final AnimalController animalController = new AnimalController();

    protected EndpointGroup getRoutes() {
        return () -> {
            // Retrieve all animals
            get("/animals", animalController::readAll, Role.USER, Role.ADMIN, Role.VETERINARIAN);

            // Retrieve specific animal by id
            get("/animals/:id", animalController::read, Role.USER, Role.ADMIN, Role.VETERINARIAN);

            // populate animals into the DB
            //get("/animals/populate", animalController::populate, Role.ADMIN);

            // Create a new animal
            post("/animals", animalController::create, Role.USER, Role.ADMIN, Role.VETERINARIAN);

            // Update animal details
            put("/animals/:id", animalController::update, Role.USER, Role.ADMIN, Role.VETERINARIAN);

            // Delete specific animal
            delete("/animals/:id", animalController::delete, Role.USER, Role.ADMIN, Role.VETERINARIAN);
        };
    }
}