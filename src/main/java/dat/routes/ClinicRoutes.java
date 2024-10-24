package dat.routes;

import dat.controllers.impl.ClinicController;
import io.javalin.apibuilder.EndpointGroup;
import static io.javalin.apibuilder.ApiBuilder.*;

public class ClinicRoutes {

    private final ClinicController clinicController = new ClinicController();

    protected EndpointGroup getRoutes() {
        return () -> {
            get("/", clinicController::readAll);              // Retrieve all clinics
            get("/{id}", clinicController::read);             // Retrieve specific clinic by id
            post("/{id}", clinicController::create);           // Create a new clinic
            put("/{id}", clinicController::update);            // Update clinic details
            delete("/{id}", clinicController::delete);         // Delete specific clinic

            //opening hours
            get("/{id}/openinghours", clinicController::addOpeningHours); // Add clinic opening hours
            post("/{id}/openinghours", clinicController::createOpeningHours); // Add clinic opening hours

//       // Veterinarian routes
//            get("/veterinarians", veterinarianController::readAll); // Retrieve all veterinarians
//            post("/veterinarians/{id}", veterinarianController::create); // Create a new veterinarian
//            delete("/veterinarians/{id}", veterinarianController::delete); // Delete specific veterinarian
        };
    }
}