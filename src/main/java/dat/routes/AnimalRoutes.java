package dat.routes;

import dat.controller.impl.AnimalController;
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
            // GET /animals - Get all animals
            get(animalController::readAll, Role.ANYONE);

            // GET /animals/{id} - Get animal by ID
            get("{id}", animalController::read, Role.ANYONE);

            // POST /animals - Create a new animal
            post(animalController::create, Role.ANYONE);

            // PUT /animals/{id} - Update an existing animal
            put("{id}", animalController::update, Role.ANYONE);

            // DELETE /animals/{id} - Delete an animal by ID
            delete("{id}", animalController::delete, Role.ANYONE);

            // GET /animals/search-by-species?species=Dog - Search animals by species
            get("/search-by-species", animalController::searchBySpecies, Role.ANYONE);

            // GET /animals/search-by-name?name=Max - Search animals by name
            get("/search-by-name", animalController::searchByName, Role.ANYONE);

            // GET /animals/{id}/medical-history - Get medical history of an animal
            get("{id}/medical-history", animalController::getMedicalHistory, Role.ANYONE);

            // POST /animals/{id}/medical-history - Add medical history entry to an animal
            post("{id}/medical-history", animalController::addMedicalHistory, Role.ANYONE);

            // GET /animals/{id}/appointments - Get all appointments for a specific animal
            get("{id}/appointments", animalController::getAnimalAppointments, Role.ANYONE);
        };
    }
}
