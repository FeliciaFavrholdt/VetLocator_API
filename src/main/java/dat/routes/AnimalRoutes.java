package dat.routes;

import dat.controllers.impl.AnimalController;
import io.javalin.apibuilder.EndpointGroup;
import static io.javalin.apibuilder.ApiBuilder.*;

public class AnimalRoutes {

    private final AnimalController animalController = new AnimalController();

    protected EndpointGroup getRoutes() {
        return () -> {
            // Retrieve all animals
            get("/animals", animalController::readAll);

            // Retrieve specific animal by id
            get("/animals/:id", animalController::read);

            // populate animals into the DB
            //get("/animals/populate", animalController::populate);

            // Create a new animal
            post("/animals", animalController::create);

            // Update animal details
            put("/animals/:id", animalController::update);

            // Delete specific animal
            delete("/animals/:id", animalController::delete);
        };
    }
}