package dat.routes;

import io.javalin.apibuilder.EndpointGroup;
import org.jetbrains.annotations.NotNull;

import static io.javalin.apibuilder.ApiBuilder.get;

public class ClientRoutes {
    public @NotNull EndpointGroup getRoutes() {
        return () -> {
            get("/clients", ctx -> {
                ctx.json("Clients");
            });
        };
    }
}
