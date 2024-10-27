package dat.routes;

import dat.controllers.impl.ClientController;
import io.javalin.apibuilder.EndpointGroup;
import static io.javalin.apibuilder.ApiBuilder.*;

public class ClientRoutes {

    private final ClientController clientController = new ClientController();

    protected EndpointGroup getRoutes() {
        return () -> {
            get("/", clientController::readAll);
            get("/{id}", clientController::read);
            post("/", clientController::create);
            put("/{id}", clientController::update);
            delete("/{id}", clientController::delete);
        };
    }
}