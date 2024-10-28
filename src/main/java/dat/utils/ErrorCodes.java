package dat.utils;

public class ErrorCodes {

    public static final String E1_MSG = "The requested resource could not be found.";
    public static final String E2_MSG = "Invalid or missing parameters in the request.";
    public static final String E3_MSG = "Authentication failure, typically due to a missing or invalid API key.";
    public static final String E4_MSG = "An unexpected error occurred on the server.";
    public static final String E5_MSG = "Bad gateway or upstream server error.";

    public static final int E1_STATUS = 404;
    public static final int E2_STATUS = 400;
    public static final int E3_STATUS = 401;
    public static final int E4_STATUS = 500;
    public static final int E5_STATUS = 502;
}

