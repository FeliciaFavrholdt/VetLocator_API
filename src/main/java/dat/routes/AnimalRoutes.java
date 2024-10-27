package dat.routes;

import dat.controllers.impl.AnimalController;
import dat.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;
import static io.javalin.apibuilder.ApiBuilder.*;

public class AnimalRoutes {
    private final AnimalController animalController = new AnimalController();

    public EndpointGroup getRoutes() {
        return () -> {
            // Open routes for any role (e.g., CLIENT, ADMIN)
            get("/", animalController::readAll, Role.ANYONE);  // GET /api/animals
            get("/{id}", animalController::read, Role.CLIENT, Role.ADMIN); // GET /api/animals/{id}

            // Restricted routes (e.g., only ADMIN can create, update, and delete)
            post("/", animalController::create, Role.ADMIN);               // POST /api/animals
            put("/{id}", animalController::update, Role.ADMIN);            // PUT /api/animals/{id}
            delete("/{id}", animalController::delete, Role.ADMIN);         // DELETE /api/animals/{id}
        };
    }
}
