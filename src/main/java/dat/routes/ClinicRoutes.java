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
            get("/", clinicController::readAll, Role.ANYONE, Role.USER, Role.ADMIN, Role.VETERINARIAN);

            // Retrieve all veterinarians
            get("/veterinarians", clinicController::readAllVets, Role.ANYONE, Role.VETERINARIAN, Role.ADMIN, Role.USER);

            // Retrieve a specific clinic by id
            get("/{id}", clinicController::read, Role.ANYONE, Role.USER, Role.ADMIN, Role.VETERINARIAN);

            // Retrieve clinic opening hours
            get("/{id}/openinghours", clinicController::readOpeningHours, Role.ANYONE, Role.USER, Role.ADMIN);

            // Populate clinic data into the DB
            get("/populate", clinicController::populate, Role.ADMIN);

            // Create a new clinic
            post("/{id}", clinicController::create, Role.ADMIN);

            // Create a new veterinarian by id
            post("/veterinarians/{id}", clinicController::createVet, Role.ADMIN);

            // Add opening hours to a clinic
            post("/{id}/openinghours", clinicController::addOpeningHours, Role.ADMIN);

            // Update clinic details
            put("/{id}", clinicController::update, Role.ADMIN);

            // Delete a specific veterinarian
            delete("/veterinarians/{id}", clinicController::deleteVet, Role.ADMIN);

            // Delete a specific clinic
            delete("/{id}", clinicController::delete, Role.ADMIN);
        };
    }
}