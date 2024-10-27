package dat.security.exceptions;

import lombok.Getter;

/**
 * Purpose: To handle validation exceptions in the API
 * Author: Thomas Hartmann
 */
@Getter

public class ValidationException extends Exception {
    public ValidationException(String message) {
        super(message);
    }
}
