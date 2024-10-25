package dat.routes;

import dat.controllers.impl.AnimalController;
import dat.controllers.impl.VetenarianController;
import dat.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;
import static io.javalin.apibuilder.ApiBuilder.delete;

public class VetenarianRoutes {

    private final VetenarianController vetenarianController = new VetenarianController();

    protected EndpointGroup getRoutes() {
        return () -> {
            // get all appointments of a vetenarian
            get("/{id}/appointments", vetenarianController::getAppointments, Role.VETERINARIAN, Role.ADMIN);

            // add appointment to a vetenarian
            post("/{id}/appointments", vetenarianController::addAppointment, Role.VETERINARIAN, Role.ADMIN);

            // update appointment of a vetenarian
            put("/{id}/appointments/{appointmentId}", vetenarianController::updateAppointment, Role.VETERINARIAN, Role.ADMIN);

            // delete appointment of a vetenarian
            delete("/{id}/appointments/{appointmentId}", vetenarianController::deleteAppointment, Role.VETERINARIAN, Role.ADMIN);
        };
    }

}