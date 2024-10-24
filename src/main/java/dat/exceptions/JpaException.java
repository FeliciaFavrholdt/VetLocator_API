package dat.exceptions;

public class JpaException extends RuntimeException {

    private final Message messageRecord;

    public JpaException(int statusCode, String message) {
        super(message);
        this.messageRecord = new Message(statusCode, message);  // Use Message record
    }

    public int getStatusCode() {
        return messageRecord.status();  // Retrieve status from Message record
    }

    @Override
    public String getMessage() {
        return messageRecord.message();  // Retrieve message from Message record
    }

    public Message getMessageRecord() {
        return messageRecord;  // Optionally expose the entire Message record
    }
}