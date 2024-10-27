package dat.routes;

import dat.controllers.impl.AppointmentController;
import dat.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;
import static io.javalin.apibuilder.ApiBuilder.*;

public class AppointmentRoutes {

    private final AppointmentController appointmentController = new AppointmentController();

    public EndpointGroup getRoutes() {
        return () -> {
            // Routes for retrieving appointments (accessible by CLIENT, VET, ADMIN)
            get("/", appointmentController::readAll, Role.CLIENT, Role.VET, Role.ADMIN); // GET /api/appointments (all appointments)
            get("/{id}", appointmentController::read, Role.CLIENT, Role.VET, Role.ADMIN); // GET /api/appointments/{id}

            // Routes for booking and managing appointments (CLIENT for booking, VET/ADMIN for managing)
            post("/", appointmentController::create, Role.CLIENT); // POST /api/appointments (create appointment)
            put("/{id}", appointmentController::update, Role.VET, Role.ADMIN); // PUT /api/appointments/{id} (update appointment)
            delete("/{id}", appointmentController::delete, Role.VET, Role.ADMIN); // DELETE /api/appointments/{id} (cancel appointment)
        };
    }
}