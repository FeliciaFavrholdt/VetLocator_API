package dat.routes;

import dat.controllers.impl.ClinicController;
import dat.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;
import static io.javalin.apibuilder.ApiBuilder.*;

public class ClinicRoutes {

    private final ClinicController clinicController = new ClinicController();

    protected EndpointGroup getRoutes() {
        return () -> {
            // Retrieve all clinics
            get("/api/clinics", clinicController::readAll, Role.ANYONE, Role.USER, Role.ADMIN, Role.VETERINARIAN);

            // Retrieve all veterinarians
            get("/api/clinics/veterinarians", clinicController::readAllVets, Role.ANYONE, Role.VETERINARIAN, Role.ADMIN, Role.USER);

            // Retrieve a specific clinic by id
            get("/api/clinics/:id", clinicController::read, Role.ANYONE, Role.USER, Role.ADMIN, Role.VETERINARIAN);

            // Retrieve clinic opening hours
            get("/api/clinics/{id}/openinghours", clinicController::readOpeningHours, Role.ANYONE, Role.USER, Role.ADMIN);

            // Populate clinic data into the DB
            get("/api/clinics/populate", clinicController::populate, Role.ADMIN);

            // Create a new clinic
            post("/api/clinics/:id", clinicController::create, Role.ADMIN);

            // Create a new veterinarian
            post("/api/clinics/veterinarians/:id", clinicController::createVet, Role.ADMIN);

            // Add opening hours to a clinic
            post("/api/clinics/:id/openinghours", clinicController::addOpeningHours, Role.ADMIN);

            // Update clinic details
            put("/api/clinics/:id", clinicController::update, Role.ADMIN);

            // Delete a specific veterinarian
            delete("/api/clinics/veterinarians/:id", clinicController::deleteVet, Role.ADMIN);

            // Delete a specific clinic
            delete("/api/clinics/:id", clinicController::delete, Role.ADMIN);
        };
    }
}