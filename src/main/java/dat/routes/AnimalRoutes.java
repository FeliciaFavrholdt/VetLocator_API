package dat.routes;

import dat.controllers.impl.AnimalController;
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
            get(animalController::readAll);

            // GET /animals/{id}
            get("{id}", animalController::read);

            // POST /animals
            post(animalController::create);

            // PUT /animals/{id}
            put("{id}", animalController::update);

            // DELETE /animals/{id}
            delete("{id}", animalController::delete);
        };
    }
}
