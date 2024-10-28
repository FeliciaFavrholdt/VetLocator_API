package dat.routes;

import dat.controller.impl.ClinicController;
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
            // GET /clinics - List all clinics
            get(clinicController::readAll, Role.ANYONE);

            // GET /clinics/{id} - Get clinic by ID
            get("{id}", clinicController::read, Role.ANYONE);

            // POST /clinics - Create a new clinic
            post(clinicController::create, Role.ANYONE);

            // PUT /clinics/{id} - Update an existing clinic
            put("{id}", clinicController::update, Role.ANYONE);

            // DELETE /clinics/{id} - Delete a clinic by ID
            delete("{id}", clinicController::delete, Role.ANYONE);

            // GET /clinics/{id}/opening-hours - Get opening hours for a clinic
            get("{id}/opening-hours", clinicController::getOpeningHours, Role.ANYONE);

            // POST /clinics/{id}/opening-hours - Add or update opening hours for a clinic
            post("{id}/opening-hours", clinicController::addOpeningHours, Role.ANYONE);

            // GET /clinics/search-by-city?cityId=1 - Search clinics by city
            get("/search-by-city", clinicController::getClinicsByCity, Role.ANYONE);

            // GET /clinics/search-by-name?name=ABC - Search clinics by name
            get("/search-by-name", clinicController::getClinicsByName, Role.ANYONE);

            // POST /clinics/{clinicId}/veterinarians/{vetId} - Add a veterinarian to a clinic
            post("{clinicId}/veterinarians/{vetId}", clinicController::addVeterinarianToClinic, Role.ANYONE);

            // GET /clinics/{clinicId}/veterinarians - Get all veterinarians of a clinic
            get("{clinicId}/veterinarians", clinicController::getVeterinariansInClinic, Role.ANYONE);

            // DELETE /clinics/{clinicId}/veterinarians/{vetId} - Remove a veterinarian from a clinic
            delete("{clinicId}/veterinarians/{vetId}", clinicController::removeVeterinarianFromClinic, Role.ANYONE);
        };
    }
}