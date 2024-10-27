package dat.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import dat.routes.Routes;
import dat.security.controllers.AccessController;
import dat.security.controllers.SecurityController;
import dat.security.enums.Role;
import dat.security.exceptions.ApiException;
import dat.security.routes.SecurityRoutes;
import dat.utils.Utils;
import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import io.javalin.http.Context;
import io.javalin.http.staticfiles.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplicationConfig {

    private static Routes routes = new Routes();
    private static ObjectMapper jsonMapper = new Utils().getObjectMapper();
    private static SecurityController securityController = SecurityController.getInstance();
    private static AccessController accessController = new AccessController();
    private static Logger logger = LoggerFactory.getLogger(ApplicationConfig.class);
    private static int count = 1;

    public static void configuration(JavalinConfig config) {
        // Disable the Javalin banner
        config.showJavalinBanner = false;

        // Enable a route overview, accessible to ANYONE
        config.bundledPlugins.enableRouteOverview("/routes", Role.ANYONE);

        // Set the base path for all API endpoints
        config.router.contextPath = "/api";

        // Register application routes
        config.router.apiBuilder(routes.getRoutes());

        // Register security routes (secured and public)
        config.router.apiBuilder(SecurityRoutes.getSecuredRoutes());
        config.router.apiBuilder(SecurityRoutes.getSecurityRoutes());

        // Serve static files from the /public directory
        config.staticFiles.add("/public", Location.CLASSPATH);
    }

    public static Javalin startServer(int port) {
        // Create Javalin instance and apply the configuration
        Javalin app = Javalin.create(ApplicationConfig::configuration);

        // Apply access control before any route matching
        app.beforeMatched(ctx -> accessController.accessHandler(ctx));

        // Log each request after it has been handled
        app.after(ApplicationConfig::afterRequest);

        // Global exception handler for generic exceptions
        app.exception(Exception.class, ApplicationConfig::generalExceptionHandler);

        // Specific exception handler for API-specific exceptions
        app.exception(ApiException.class, ApplicationConfig::apiExceptionHandler);

        // Start the server on the specified port
        app.start(port);

        return app;
    }

    public static void afterRequest(Context ctx) {
        // Log each request with its method, URI, and the response status code
        String requestInfo = ctx.req().getMethod() + " " + ctx.req().getRequestURI();
        logger.info(" Request {} - {} was handled with status code {}", count++, requestInfo, ctx.status());
    }

    public static void stopServer(Javalin app) {
        // Stop the Javalin server
        app.stop();
    }

    private static void generalExceptionHandler(Exception e, Context ctx) {
        // Handle all unhandled exceptions by logging and responding with a JSON message
        logger.error("An unhandled exception occurred: {}", e.getMessage());
        ctx.json(Utils.convertToJsonMessage(ctx, "error", e.getMessage()));
    }

    public static void apiExceptionHandler(ApiException e, Context ctx) {
        // Handle API-specific exceptions by logging the issue and responding with JSON
        ctx.status(e.getStatusCode());
        logger.warn("An API exception occurred: Code: {}, Message: {}", e.getStatusCode(), e.getMessage());
        ctx.json(Utils.convertToJsonMessage(ctx, "warning", e.getMessage()));
    }
}