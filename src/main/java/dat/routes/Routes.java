package dat.routes;

import io.javalin.apibuilder.EndpointGroup;
import static io.javalin.apibuilder.ApiBuilder.*;

public class Routes {

    private final AnimalRoutes animalRoute = new AnimalRoutes();
    private final ClientRoutes userRoute = new ClientRoutes();
    private final ClinicRoutes clinicRoute = new ClinicRoutes();

    public EndpointGroup getRoutes() {
        return () -> {
            path("/animals", animalRoute.getRoutes());
            path("/users", userRoute.getRoutes());
            path("/clinics", clinicRoute.getRoutes());
        };
    }
}
