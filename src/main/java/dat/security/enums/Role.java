package dat.security.enums;

import io.javalin.security.RouteRole;

public enum Role implements RouteRole {
    ANYONE, CLIENT, ADMIN, VET;
}
