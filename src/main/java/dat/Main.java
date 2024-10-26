package dat;

import dat.config.ApplicationConfig;
import dat.controllers.impl.ExceptionController;
import dat.enums.Weekday;
import dat.exceptions.ApiException;
import dat.exceptions.Message;
import dat.routes.Routes;
import io.javalin.Javalin;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {
        ApplicationConfig.startServer(7071);

    }
}