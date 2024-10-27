package dat.routes;

import io.javalin.apibuilder.EndpointGroup;
import static io.javalin.apibuilder.ApiBuilder.*;

public class Routes {

    private final AnimalRoutes animalRoute = new AnimalRoutes();
    private final ClinicRoutes clinicRoute = new ClinicRoutes();
    private final ClientRoutes clientRoutes = new ClientRoutes();

    public EndpointGroup getRoutes() {
        return () -> {
            // Redirect root path to the index.html page (public access)
            get("/", ctx -> ctx.redirect("/api/index.html"), Role.ANYONE);

            // Grouped routes for animals, clients, and clinics
            path("/animals", animalRoute.getRoutes());
            path("/clients", clientRoutes.getRoutes());
            path("/clinics", clinicRoute.getRoutes());
        };
    }
}