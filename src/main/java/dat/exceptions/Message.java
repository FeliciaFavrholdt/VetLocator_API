package dat.exceptions;

public record Message(int status, String message) {

    public Message {
        if (status < 100 || status > 599) {
            throw new IllegalArgumentException("Invalid HTTP status code: " + status);
        }
        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException("Message cannot be null or blank");
        }
    }

    public static Message success(String message) {
        return new Message(200, message);
    }

    public static Message error(int statusCode, String errorMessage) {
        return new Message(statusCode, errorMessage);
    }

    @Override
    public String toString() {
        return "Message[status=" + status + ", message=\"" + message + "\"]";
    }
}
