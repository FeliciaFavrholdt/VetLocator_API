package dat.security.exceptions;

import dat.utils.Utils;

/**
 * Purpose: To handle exceptions in the API
 * Author: Thomas Hartmann
 */
public class ApiException extends RuntimeException {
    private int code;
    public ApiException (int code, String msg) {
        super(msg);
        this.code = code;
    }

    public int getStatusCode() {
        return code;
    }

    public Object getMessageRecord() {
        return Utils.convertToJsonMessage(null, "error", getMessage());
    }
}
