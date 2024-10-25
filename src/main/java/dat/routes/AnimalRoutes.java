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
            get("/", animalController::readAll, Role.ANYONE, Role.USER, Role.ADMIN, Role.VETERINARIAN);

            // Retrieve specific animal by id
            get("/{id}", animalController::read, Role.USER, Role.ADMIN, Role.VETERINARIAN);

            // populate animals into the DB
            //get("/animals/populate", animalController::populate, Role.ADMIN);

            // Create a new animal
            post("/", animalController::create, Role.USER, Role.ADMIN, Role.VETERINARIAN);

            // Update animal details
            put("/{id}", animalController::update, Role.USER, Role.ADMIN, Role.VETERINARIAN);

            // Delete specific animal
            delete("/{id}", animalController::delete, Role.USER, Role.ADMIN, Role.VETERINARIAN);
        };
    }
}