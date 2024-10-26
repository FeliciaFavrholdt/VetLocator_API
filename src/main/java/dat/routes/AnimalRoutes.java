package dat.routes;

import dat.controllers.impl.AnimalController;
import io.javalin.apibuilder.EndpointGroup;
import static io.javalin.apibuilder.ApiBuilder.*;

public class AnimalRoutes {
    private final AnimalController animalController = new AnimalController();

    public EndpointGroup getRoutes() {
        return () -> {
            get("/", animalController::readAll);   // GET /api/animals
            get("/{id}", animalController::read);  // GET /api/animals/{id}
            post("/", animalController::create);   // POST /api/animals
            put("/{id}", animalController::update);// PUT /api/animals/{id}
            delete("/{id}", animalController::delete); // DELETE /api/animals/{id}
        };
    }
}