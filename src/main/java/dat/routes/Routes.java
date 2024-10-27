package dat.routes;

import io.javalin.apibuilder.EndpointGroup;
import static io.javalin.apibuilder.ApiBuilder.*;

public class Routes {

    private final AnimalRoutes animalRoute = new AnimalRoutes();
    private final ClinicRoutes clinicRoute = new ClinicRoutes();
    private final ClientRoutes clientRoutes = new ClientRoutes();

    public EndpointGroup getRoutes() {
        return () -> {
            // Define the root path to serve the index.html page
            path("/", () -> {
                get(ctx -> {
                    // Redirect to /index.html, which is served from /public directory
                    ctx.redirect("/api/index.html");
                });
            });

            // Animal routes
            path("/animals", animalRoute.getRoutes());

            // Client routes
            path("/clients", clientRoutes.getRoutes());

            // Clinic routes
            path("/clinics", clinicRoute.getRoutes());
        };
    }
}
