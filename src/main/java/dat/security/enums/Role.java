package dat.security.enums;

public enum Role implements io.javalin.security.RouteRole {
    ANYONE, CLIENT, ADMIN, VET;
}
