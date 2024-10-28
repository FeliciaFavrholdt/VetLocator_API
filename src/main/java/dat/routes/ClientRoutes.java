package dat.routes;

import dat.controllers.impl.ClientController;
import dat.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;
import static io.javalin.apibuilder.ApiBuilder.*;

public class ClientRoutes {

    private final ClientController clientController;

    public ClientRoutes() {
        this.clientController = new ClientController();
    }

    public EndpointGroup getRoutes() {
        return () -> {
            // GET /clients - Get all clients
            get(clientController::readAll, Role.ANYONE);

            // GET /clients/{id} - Get client by ID
            get("{id}", clientController::read, Role.ANYONE);

            // POST /clients - Create a new client
            post(clientController::create, Role.ANYONE);

            // PUT /clients/{id} - Update an existing client
            put("{id}", clientController::update, Role.ADMIN);

            // DELETE /clients/{id} - Delete a client
            delete("{id}", clientController::delete, Role.ADMIN);

            // GET /clients/search-by-email?email=email@example.com - Search client by email
            get("/search-by-email", clientController::searchByEmail, Role.ADMIN, Role.VET);

            // GET /clients/{id}/animals - Get all animals belonging to a specific client
            get("{id}/animals", clientController::getClientAnimals, Role.ADMIN, Role.VET);

            // POST /clients/{id}/animals - Add a new animal to a client
            post("{id}/animals", clientController::addAnimalToClient, Role.ADMIN, Role.VET);

            // GET /clients/{id}/appointments - Get all appointments for a specific client
            get("{id}/appointments", clientController::getClientAppointments, Role.ADMIN, Role.VET);
        };
    }
}
