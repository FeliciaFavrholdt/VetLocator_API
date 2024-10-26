package dat.routes;

import io.javalin.apibuilder.EndpointGroup;
import static io.javalin.apibuilder.ApiBuilder.*;

public class Routes {

    private final AnimalRoutes animalRoute = new AnimalRoutes();
    private final ClinicRoutes clinicRoute = new ClinicRoutes();
    private final ClientRoutes clientRoutes = new ClientRoutes();

    public EndpointGroup getRoutes() {
        return () -> {
            path("/animals", animalRoute.getRoutes());
            path("/clients", clientRoutes.getRoutes());
            path("/clinics", clinicRoute.getRoutes());
        };
    }
}
