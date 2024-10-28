package dat.routes;

import io.javalin.apibuilder.EndpointGroup;
import org.jetbrains.annotations.NotNull;

import static io.javalin.apibuilder.ApiBuilder.get;

public class ClinicRoutes {
    public @NotNull EndpointGroup getRoutes() {
        return () -> {
            get("/clinics", ctx -> {
                ctx.json("Clinics");
            });
        };
    }
}

