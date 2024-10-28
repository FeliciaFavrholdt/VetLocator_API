package dat.routes;

import dat.controllers.impl.ClientController;
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
            get(clientController::readAll);

            // GET /clients/{id}
            get("{id}", clientController::read);

            // POST /clients
            post(clientController::create);

            // PUT /clients/{id}
            put("{id}", clientController::update);

            // DELETE /clients/{id}
            delete("{id}", clientController::delete);
        };
    }
}
