package dat.util;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dat.exceptions.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.javalin.http.Context;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Utils {

    private static final Logger logger = LoggerFactory.getLogger(Utils.class);

    public static void main(String[] args) {
        System.out.println(getPropertyValue("DB_NAME", "config.properties"));
    }

    public static String getPropertyValue(String propName, String resourceName) {
        try (InputStream is = Utils.class.getClassLoader().getResourceAsStream(resourceName)) {
            Properties prop = new Properties();
            prop.load(is);

            String value = prop.getProperty(propName);
            if (value != null) {
                return value.trim();
            } else {
                logger.error("Property '{}' not found in '{}'", propName, resourceName);
                throw new ApiException(ErrorCodes.E4_STATUS, String.format("Property %s not found in %s", propName, resourceName));
            }
        } catch (IOException ex) {
            logger.error("Error reading property '{}': {}", propName, ex.getMessage());
            throw new ApiException(ErrorCodes.E4_STATUS, String.format("Could not read property %s. Ensure the project is built with Maven.", propName));
        }
    }

    public ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.writer(new DefaultPrettyPrinter());
        return objectMapper;
    }

    public static String convertToJsonMessage(Context ctx, String property, String message) {
        Map<String, String> msgMap = new HashMap<>();
        msgMap.put(property, message);
        msgMap.put("status", String.valueOf(ctx.status()));
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(msgMap);
        } catch (Exception e) {
            logger.error("Error converting message to JSON: {}", e.getMessage());
            return "{\"error\": \"Could not convert message to JSON\"}";
        }
    }
}
