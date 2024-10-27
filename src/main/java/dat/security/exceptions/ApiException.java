package dat.security.exceptions;

import dat.util.Utils;
import lombok.Getter;

import java.util.Map;

/**
 * Purpose: To handle exceptions in the API
 * Author: Thomas Hartmann
 */
@Getter
public class ApiException extends RuntimeException {
    private int code;

    public ApiException (int code, String msg) {
        super(msg);
        this.code = code;
    }

    public int getStatusCode() {
        return code;
    }
    public Map<String, String> getMessageRecord() {
        return Map.of("status", String.valueOf(code), "message", getMessage());
    }
}
