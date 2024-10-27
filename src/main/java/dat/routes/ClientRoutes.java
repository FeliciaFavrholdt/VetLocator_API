package dat.routes;

import dat.controllers.impl.ClientController;
import dat.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;
import static io.javalin.apibuilder.ApiBuilder.*;

public class ClientRoutes {

    private final ClientController clientController = new ClientController();

    protected EndpointGroup getRoutes() {
        return () -> {
            // Routes accessible by CLIENT and ADMIN roles
            get("/", clientController::readAll, Role.CLIENT, Role.ADMIN);   // GET /api/clients
            get("/{id}", clientController::read, Role.CLIENT, Role.ADMIN);  // GET /api/clients/{id}

            // Restricted routes, accessible only by ADMIN role
            post("/", clientController::create, Role.ADMIN);                // POST /api/clients
            put("/{id}", clientController::update, Role.ADMIN);             // PUT /api/clients/{id}
            delete("/{id}", clientController::delete, Role.ADMIN);          // DELETE /api/clients/{id}
        };
    }
}