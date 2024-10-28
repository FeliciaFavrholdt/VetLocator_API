package dat.security.controllers;

import dat.exceptions.ApiException;
import dat.exceptions.Message;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExceptionController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionController.class);

    /**
     * Handles API-specific exceptions and logs them with appropriate details.
     * @param e the ApiException
     * @param ctx the Javalin context
     */
    public void apiExceptionHandler(ApiException e, Context ctx) {
        LOGGER.error(ctx.attribute("requestInfo") + " " + ctx.res().getStatus() + " " + e.getMessage());
        ctx.status(e.getStatusCode());
        ctx.json(new Message(e.getStatusCode(), e.getMessage()));
    }

    /**
     * Handles general exceptions and logs them with appropriate details.
     * @param e the Exception
     * @param ctx the Javalin context
     */
    public void exceptionHandler(Exception e, Context ctx) {
        LOGGER.error(ctx.attribute("requestInfo") + " " + ctx.res().getStatus() + " " + e.getMessage());
        ctx.status(500);
        ctx.json(new Message(500, "An unexpected error occurred: " + e.getMessage()));
    }
}