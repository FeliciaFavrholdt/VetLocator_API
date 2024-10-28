package dat.routes;

import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;
import static io.javalin.apibuilder.ApiBuilder.*;

public class Routes {

    private final ClinicRoutes clinicRoutes;

    public Routes(EntityManagerFactory emf) {
        clinicRoutes = new ClinicRoutes();
    }

    public EndpointGroup getRoutes() {

        return () -> {
            // Redirect root path to the index.html page (public access)
            get("/", ctx -> ctx.redirect("/api/index.html"));
            path("clinics", clinicRoutes.getRoutes());
        };
    }
}