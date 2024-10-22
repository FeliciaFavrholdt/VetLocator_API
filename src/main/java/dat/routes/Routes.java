package dat.routes;

import io.javalin.apibuilder.EndpointGroup;
import static io.javalin.apibuilder.ApiBuilder.*;

public class Routes {

    public EndpointGroup getRoutes() {
        return () -> {
            path("/animals", animalRoute.getRoutes());
            path("/users", userRoute.getRoutes());
            path("/clinics", clinicRoute.getRoutes());
        };
    }
}