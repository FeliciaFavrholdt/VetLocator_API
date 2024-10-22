package dat.routes;

import io.javalin.apibuilder.EndpointGroup;

public class AnimalRoutes {

    private final AnimalController animalController = new AnimalController();

    protected EndpointGroup getRoutes() {
        return () -> {
            get("/populate", animalController::populate); // Populate animals into DB
            post("/", animalController::create);          // Create a new animal
            get("/", animalController::readAll);          // Retrieve all animals
            get("/{id}", animalController::read);         // Retrieve specific animal by id
            put("/{id}", animalController::update);       // Update animal details
            delete("/{id}", animalController::delete);    // Delete specific animal
        };
    }
}