package dat.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApiException extends RuntimeException {

    private static final Logger logger = LoggerFactory.getLogger(ApiException.class);
    private final Message messageRecord;

    public ApiException(int statusCode, String message) {
        super(message);
        this.messageRecord = new Message(statusCode, message);

        // Log the error when an ApiException is created
        logger.error("ApiException occurred: StatusCode={}, Message={}", statusCode, message);
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
