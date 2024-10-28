package dat.routes;

import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;
import static io.javalin.apibuilder.ApiBuilder.*;

public class Routes {

    private final ClinicRoutes clinicRoutes;
    private final AnimalRoutes animalRoutes;
    private final ClientRoutes clientRoutes;

    public Routes(EntityManagerFactory emf) {
        clinicRoutes = new ClinicRoutes();
        animalRoutes = new AnimalRoutes();
        clientRoutes = new ClientRoutes();
    }

    public EndpointGroup getRoutes() {
        return () -> {
            path("/clinics", clinicRoutes.getRoutes());
            path("/animals", animalRoutes.getRoutes());
            path("/clients", clientRoutes.getRoutes());
        };

    }
}