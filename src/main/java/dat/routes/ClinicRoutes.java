package dat.routes;

import dat.controllers.impl.ClinicController;
import io.javalin.apibuilder.EndpointGroup;
import static io.javalin.apibuilder.ApiBuilder.*;

public class ClinicRoutes {

    private final ClinicController clinicController;

    public ClinicRoutes() {
        this.clinicController = new ClinicController(); // Assume it uses EntityManagerFactory internally
    }

    public EndpointGroup getRoutes() {
        return () -> {
            // GET /clinics
            get(clinicController::readAll);

            // GET /clinics/{id}
            get("{id}", clinicController::read);

            // POST /clinics
            post(clinicController::create);

            // PUT /clinics/{id}
            put("{id}", clinicController::update);

            // DELETE /clinics/{id}
            delete("{id}", clinicController::delete);
        };
    }
}