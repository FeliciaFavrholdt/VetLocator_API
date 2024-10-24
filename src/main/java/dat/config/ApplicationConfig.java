package dat.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import dat.routes.Routes;
import dat.security.controllers.AccessController;
import dat.security.controllers.SecurityController;
import dat.security.enums.Role;
import dat.security.exceptions.ApiException;
import dat.exceptions.Message;
import dat.security.routes.SecurityRoutes;
import dat.utils.Utils;
import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplicationConfig {

    private static final Routes routes = new Routes();
    private static final ObjectMapper jsonMapper = new Utils().getObjectMapper();
    private static final SecurityController securityController = SecurityController.getInstance();
    private static final AccessController accessController = new AccessController();
    private static final Logger logger = LoggerFactory.getLogger(ApplicationConfig.class);
    private static int count = 1;

    /**
     * Javalin configuration settings
     */
    public static void configuration(JavalinConfig config) {
        config.showJavalinBanner = false;
        config.bundledPlugins.enableRouteOverview("/routes", Role.ANYONE);
        config.router.contextPath = "/api"; // Base path for all endpoints
        config.router.apiBuilder(routes.getRoutes());
        config.router.apiBuilder(SecurityRoutes.getSecuredRoutes());
        config.router.apiBuilder(SecurityRoutes.getSecurityRoutes());
    }

    /**
     * Starts the Javalin server
     * @param port The port number to bind the server to
     * @return Javalin instance
     */
    public static Javalin startServer(int port) {
        Javalin app = Javalin.create(ApplicationConfig::configuration);

        // Access control for requests
        app.beforeMatched(accessController::accessHandler);

        // Log after each request is handled
        app.after(ApplicationConfig::afterRequest);

        // Global exception handlers
        app.exception(Exception.class, ApplicationConfig::generalExceptionHandler);
        app.exception(ApiException.class, ApplicationConfig::apiExceptionHandler);

        // Start the server
        app.start(port);
        return app;
    }

    /**
     * Handles after request logging.
     * @param ctx The context of the HTTP request.
     */
    public static void afterRequest(Context ctx) {
        String requestInfo = ctx.req().getMethod() + " " + ctx.req().getRequestURI();
        String clientIP = ctx.req().getRemoteAddr();
        logger.info("Request {} - {} from IP: {} was handled with status code: {}", count++, requestInfo, clientIP, ctx.status());
    }

    /**
     * Stops the Javalin server
     * @param app The Javalin instance
     */
    public static void stopServer(Javalin app) {
        app.stop();
    }

    /**
     * General exception handler for uncaught exceptions.
     * @param e The caught exception.
     * @param ctx The context of the HTTP request.
     */
    private static void generalExceptionHandler(Exception e, Context ctx) {
        logger.error("An unhandled exception occurred: {}", e.getMessage(), e);
        // Set response status to 500 and return a new Message record
        ctx.status(500);
        ctx.json(new Message(500, "Internal Server Error: " + e.getMessage()));
    }

    /**
     * Exception handler for API-specific exceptions.
     * @param e The API exception.
     * @param ctx The context of the HTTP request.
     */
    public static void apiExceptionHandler(ApiException e, Context ctx) {
        ctx.status(e.getCode());
        logger.warn("API Exception occurred: Code: {}, Message: {}", e.getCode(), e.getMessage());
        ctx.json(new Message(e.getCode(), e.getMessage()));
    }
}
