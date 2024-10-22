package dat.routes;

import dat.controllers.impl.AnimalController;
import io.javalin.apibuilder.EndpointGroup;
import static io.javalin.apibuilder.ApiBuilder.*;

public class AnimalRoutes {

    private final AnimalController animalController = new AnimalController();

    protected EndpointGroup getRoutes() {
        return () -> {
            post("/", animalController::create);          // Create a new animal
            get("/", animalController::readAll);          // Retrieve all animals
            get("/{id}", animalController::read);         // Retrieve specific animal by id
            put("/{id}", animalController::update);       // Update animal details
            delete("/{id}", animalController::delete);    // Delete specific animal
        };
    }
}