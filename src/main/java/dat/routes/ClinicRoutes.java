package dat.routes;

import dat.controllers.impl.ClinicController;
import dat.security.enums.Role;
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
            get(clinicController::readAll, Role.ANYONE);

            // GET /clinics/{id}
            get("{id}", clinicController::read, Role.ANYONE);

            // POST /clinics
            post(clinicController::create, Role.ANYONE);

            // PUT /clinics/{id}
            put("{id}", clinicController::update, Role.ANYONE);

            // DELETE /clinics/{id}
            delete("{id}", clinicController::delete, Role.ANYONE);

            // GET/clinics/:id/opening-hours
            get("{id}/opening-hours", clinicController::getOpeningHours, Role.ANYONE);

            // GET /clinics/search-by-city?cityId=1
            get("/search-by-city", clinicController::getClinicsByCity, Role.ANYONE);

        };
    }
}