package dat.routes;

import dat.controllers.impl.ClinicController;
import io.javalin.apibuilder.EndpointGroup;
import static io.javalin.apibuilder.ApiBuilder.*;

public class ClinicRoutes {

    private final ClinicController clinicController = new ClinicController();

    protected EndpointGroup getRoutes() {
        return () -> {
            get("/", clinicController::readAll);
            get("/{id}", clinicController::read);
            post("/", clinicController::create);
            put("/{id}", clinicController::update);
            delete("/{id}", clinicController::delete);
        };
    }
}