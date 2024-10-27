package dat.config;

import dat.routes.Routes;
import dat.security.enums.Role;
import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import jakarta.persistence.EntityManagerFactory;

public class ApplicationConfig {

    private static Routes routes;

    public static void configuration(JavalinConfig config) {
        config.router.contextPath = "/api/"; // base path for all routes
        config.showJavalinBanner = false;
        config.bundledPlugins.enableRouteOverview("/routes", Role.ANYONE);
        config.http.defaultContentType = "application/json"; // default content type for requests
        config.router.apiBuilder(routes.getRoutes());

        // Plugins
        config.bundledPlugins.enableRouteOverview("/routes"); // enables route overview at /routes
    }

    public static Javalin startServer(int port, EntityManagerFactory emf) {
        routes = new Routes(emf);
        var app = Javalin.create(ApplicationConfig::configuration);
        app.start(port);
        return app;
    }

    public static void stopServer(Javalin app) {
        app.stop();
    }
}