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
            // GET /clients
            get(clientController::readAll, Role.ANYONE);

            // GET /clients/{id}
            get("{id}", clientController::read, Role.ANYONE);

            // POST /clients
            post(clientController::create, Role.ANYONE);

            // PUT /clients/{id}
            put("{id}", clientController::update, Role.ANYONE);

            // DELETE /clients/{id}
            delete("{id}", clientController::delete, Role.ANYONE);
        };
    }
}
