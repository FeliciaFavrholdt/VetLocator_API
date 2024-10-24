package dat.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import dat.controllers.impl.ExceptionController;
import dat.routes.Routes;
import dat.security.controllers.AccessController;
import dat.security.controllers.SecurityController;
import dat.security.enums.Role;
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
    private static final ExceptionController exceptionController = new ExceptionController();  // Use the ExceptionController
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

        // Global exception handlers delegated to ExceptionController
        app.exception(Exception.class, exceptionController::exceptionHandler);
        app.exception(dat.exceptions.ApiException.class, exceptionController::apiExceptionHandler);

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
}