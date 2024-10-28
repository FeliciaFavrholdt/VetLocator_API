package dat.routes;

import dat.controllers.impl.AnimalController;
import dat.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;
import static io.javalin.apibuilder.ApiBuilder.*;

public class AnimalRoutes {

    private final AnimalController animalController;

    public AnimalRoutes() {
        this.animalController = new AnimalController();
    }

    public EndpointGroup getRoutes() {
        return () -> {
            // GET /animals
            get(animalController::readAll, Role.ANYONE);

            // GET /animals/{id}
            get("{id}", animalController::read, Role.ANYONE);

            // POST /animals
            post(animalController::create, Role.ANYONE);

            // PUT /animals/{id}
            put("{id}", animalController::update, Role.ANYONE);

            // DELETE /animals/{id}
            delete("{id}", animalController::delete, Role.ANYONE);
        };
    }
}
