package dat.routes;

import dat.controllers.impl.ClinicController;
import dat.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;
import static io.javalin.apibuilder.ApiBuilder.*;

public class ClinicRoutes {

    private final ClinicController clinicController = new ClinicController();

    protected EndpointGroup getRoutes() {
        return () -> {
            // Routes accessible by CLIENT, VET, and ADMIN roles
            get("/", clinicController::readAll, Role.CLIENT, Role.VET, Role.ADMIN);  // Retrieve all clinics
            get("/clinicsonduty", clinicController::getOnDutyClinics, Role.CLIENT, Role.VET, Role.ADMIN); // Retrieve clinics on duty
            get("/{id}", clinicController::read, Role.CLIENT, Role.VET, Role.ADMIN); // Retrieve specific clinic by id

            // Restricted routes, accessible only by ADMIN and VET roles
            post("/{id}", clinicController::create, Role.VET, Role.ADMIN);          // Create a new clinic
            put("/{id}", clinicController::update, Role.VET, Role.ADMIN);           // Update clinic details
            delete("/{id}", clinicController::delete, Role.VET, Role.ADMIN);        // Delete specific clinic
        };
    }
}