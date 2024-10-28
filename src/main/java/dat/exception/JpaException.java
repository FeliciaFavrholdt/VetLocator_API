package dat.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JpaException extends RuntimeException {

    private static final Logger logger = LoggerFactory.getLogger(JpaException.class);
    private final Message messageRecord;

    public JpaException(int statusCode, String message) {
        super(message);
        this.messageRecord = new Message(statusCode, message);

        // Log the error when a JpaException is created
        logger.error("JpaException occurred: StatusCode={}, Message={}", statusCode, message);
    }

    public int getStatusCode() {
        return messageRecord.status();
    }

    @Override
    public String getMessage() {
        return messageRecord.message();
    }

    public Message getMessageRecord() {
        return messageRecord;
    }
}