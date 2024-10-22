package dat.routes;

import io.javalin.apibuilder.EndpointGroup;

public class ClinicRoutes {

    private final ClinicController clinicController = new ClinicController();

    protected EndpointGroup getRoutes() {
        return () -> {
            get("/", clinicController::readAll);              // Retrieve all clinics
            get("/veterinarians", clinicController::readAllVets); // Retrieve all veterinarians
            get("/{id}", clinicController::read);             // Retrieve specific clinic by id
            get("/openinghours", clinicController::readOpeningHours); // Retrieve clinic opening hours
            get("/populate", clinicController::populate);      // Populate clinic data into DB
            post("/{id}", clinicController::create);           // Create a new clinic
            post("/veterinarians/{id}", clinicController::createVet); // Create a new veterinarian
            post("/{id}/openinghours", clinicController::addOpeningHours); // Add opening hours to a clinic
            put("/{id}", clinicController::update);            // Update clinic details
            delete("/veterinarians/{id}", clinicController::deleteVet); // Delete specific veterinarian
            delete("/{id}", clinicController::delete);         // Delete specific clinic
        };
    }
}