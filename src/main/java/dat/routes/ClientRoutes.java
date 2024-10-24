package dat.routes;

import dat.controllers.impl.ClientController;
import io.javalin.apibuilder.EndpointGroup;
import static io.javalin.apibuilder.ApiBuilder.*;

public class ClientRoutes {

    private final ClientController userController = new ClientController();

    protected EndpointGroup getRoutes() {
        return () -> {
            get("/", userController::readAll);
            get("/{id}", userController::read);
            post("/", userController::create);
            put("/{id}", userController::update);
            delete("/{id}", userController::delete);
        };
    }
}