package dat.routes;

import dat.controllers.impl.OpeningHoursController;
import dat.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;
import static io.javalin.apibuilder.ApiBuilder.*;

public class OpeningHoursRoutes {

    private final OpeningHoursController openingHoursController = new OpeningHoursController();

    public EndpointGroup getRoutes() {
        return () -> {
            // Routes for retrieving opening hours (accessible by CLIENT, VET, ADMIN)
            get("/", openingHoursController::readAll, Role.CLIENT, Role.VET, Role.ADMIN);      // GET /api/openinghours
            get("/{clinicId}", openingHoursController::read, Role.CLIENT, Role.VET, Role.ADMIN);  // GET /api/openinghours/{clinicId}

            // Routes for modifying opening hours (accessible only by VET and ADMIN)
            post("/{clinicId}", openingHoursController::create, Role.VET, Role.ADMIN);        // POST /api/openinghours/{clinicId}
            put("/{clinicId}", openingHoursController::update, Role.VET, Role.ADMIN);         // PUT /api/openinghours/{clinicId}
            delete("/{clinicId}", openingHoursController::delete, Role.VET, Role.ADMIN);      // DELETE /api/openinghours/{clinicId}
        };
    }
}