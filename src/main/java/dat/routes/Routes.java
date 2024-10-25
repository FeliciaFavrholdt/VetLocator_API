package dat.routes;

import dat.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;
import static io.javalin.apibuilder.ApiBuilder.*;

public class Routes {

    private final AnimalRoutes animalRoutes = new AnimalRoutes();
    private final ClientRoutes clientRoutes = new ClientRoutes();
    private final ClinicRoutes clinicRoutes = new ClinicRoutes();

    public EndpointGroup getRoutes() {
        return () -> {
            path("/users", clientRoutes.getRoutes());
            path("/clinics", clinicRoutes.getRoutes());
        };
    }
}
