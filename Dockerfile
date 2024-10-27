# Start with Amazon Corretto 17 Alpine base image
FROM amazoncorretto:17-alpine

# Install curl on Alpine
RUN apk update && apk add --no-cache curl

# Copy the jar file into the image
COPY target/app.jar /app.jar

# Expose the port your app runs on
EXPOSE 7071

# Command to run your app
CMD ["java", "-jar", "/app.jar"]

# Run the Populate class
ENTRYPOINT ["sh", "-c", "java -jar /app.jar" ]