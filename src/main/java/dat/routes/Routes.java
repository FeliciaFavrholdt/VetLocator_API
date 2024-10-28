package dat.routes;

import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Routes {

    private final ClinicRoutes clinicRoutes;
    private final AnimalRoutes animalRoutes;
    private final ClientRoutes clientRoutes;

    public Routes() {
        clinicRoutes = new ClinicRoutes();
        animalRoutes = new AnimalRoutes();
        clientRoutes = new ClientRoutes();
    }

    public EndpointGroup getRoutes() {
        return () -> {
            // Define the root path to serve the index.html page
            path("/", () -> {
                get(ctx -> {
                    // Redirect to /index.html, which is served from /public directory
                    ctx.redirect("/api/index.html");
                });
            });

            path("/clinics", clinicRoutes.getRoutes());
            path("/animals", animalRoutes.getRoutes());
            path("/clients", clientRoutes.getRoutes());
        };

    }
}