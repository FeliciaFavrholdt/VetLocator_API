package dat.routes;

import io.javalin.apibuilder.EndpointGroup;
import org.jetbrains.annotations.NotNull;
import static io.javalin.apibuilder.ApiBuilder.get;

public class AnimalRoutes {
    public @NotNull EndpointGroup getRoutes() {
        return () -> {
            get("/animals", ctx -> {
                ctx.json("Animals");
            });
        };
    }
}