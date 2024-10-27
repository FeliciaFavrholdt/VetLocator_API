package dat.exception;

/**
 * A simple message class to return a status code and message.
 * Immutability: Once created, a Message record cannot be modified.
 * Conciseness: A Java record reduces boilerplate code.
 *
 * @param status  the HTTP status code
 * @param message the message string
 */

public record Message(int status, String message) {

    // Constructor with validation logic
    public Message {
        if (status < 100 || status > 599) {
            throw new IllegalArgumentException("Invalid HTTP status code: " + status);
        }
        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException("Message cannot be null or blank");
        }
    }

    // Method to create a success message
    public static Message success(String message) {
        return new Message(200, message);
    }

    // Method to create an error message
    public static Message error(int statusCode, String errorMessage) {
        return new Message(statusCode, errorMessage);
    }

    // The toString method is overridden to provide a more readable output
    @Override
    public String toString() {
        return "Message[status=" + status + ", message=\"" + message + "\"]";
    }
}