package dat.dto;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

public class AnimalDTOTest {

    private static final String BASE_URL = "http://localhost:7071/api";
    private static Long createdAnimalId;
    private static final Logger logger = LoggerFactory.getLogger(AnimalDTOTest.class);

    @BeforeAll
    public static void setup() {
        // Set the base URL for the API
        RestAssured.baseURI = BASE_URL;
    }

    @BeforeEach
    public void setupTestData() {
        // Create a sample AnimalDTO to be used in tests
        AnimalDTO animalDTO = AnimalDTO.builder()
                .name("Rex")
                .species("Dog")
                .breed("Golden Retriever")
                .age(5)
                .ownerId(1L)  // Assuming owner with ID 1 exists
                .medicalHistory("VACCINATED")
                .build();

        // Make the POST request to create a new animal and get the ID for further tests
        createdAnimalId = given()
                .contentType(ContentType.JSON)
                .body(animalDTO)
                .when()
                .post("/animals")
                .then()
                .statusCode(201)  // Ensure creation was successful
                .extract().path("id");  // Extract and store the created animal's ID

        logger.info("Created Animal ID: " + createdAnimalId);
    }

    @AfterEach
    public void cleanupTestData() {
        if (createdAnimalId != null) {
            // Cleanup: Delete the created animal after each test
            given()
                    .when()
                    .delete("/animals/" + createdAnimalId)
                    .then()
                    .statusCode(204);  // Expecting status 204 No Content after successful deletion

            // Verify that the animal has been deleted
            given()
                    .when()
                    .get("/animals/" + createdAnimalId)
                    .then()
                    .statusCode(404);  // Expecting status 404 Not Found after deletion
        }
    }

    @Test
    void getAnimalById() {
        // Make a GET request to retrieve the animal by ID and check the response
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/animals/" + createdAnimalId)
                .then()
                .statusCode(200)  // Expecting status code 200 OK
                .body("id", equalTo(createdAnimalId.intValue()))
                .body("name", equalTo("Rex"))
                .body("species", equalTo("Dog"))
                .body("breed", equalTo("Golden Retriever"))
                .body("age", equalTo(5))
                .body("medicalHistory", equalTo("VACCINATED"));
    }

    @Test
    void updateAnimal() {
        // Create the updated AnimalDTO
        AnimalDTO updatedAnimalDTO = AnimalDTO.builder()
                .name("Max")
                .species("Dog")
                .breed("Labrador")
                .age(6)
                .ownerId(1L)
                .medicalHistory("SPAYED")
                .build();

        // Make a PUT request to update the animal and check the response
        AnimalDTO updatedAnimal = given()
                .contentType(ContentType.JSON)
                .body(updatedAnimalDTO)
                .when()
                .put("/animals/" + createdAnimalId)
                .then()
                .statusCode(200)  // Expecting status code 200 OK
                .body("name", equalTo("Max"))
                .body("species", equalTo("Dog"))
                .body("breed", equalTo("Labrador"))
                .body("age", equalTo(6))
                .body("medicalHistory", equalTo("SPAYED"))
                .extract().body().as(AnimalDTO.class);  // Extract the response as an AnimalDTO

        // Assertions: Check if the updated animal matches the DTO we sent
        assertNotNull(updatedAnimal.getId(), "Animal ID should not be null after update");
        assertEquals("Max", updatedAnimal.getName(), "Expected and actual name should match");
        assertEquals("Dog", updatedAnimal.getSpecies(), "Expected and actual species should match");
        assertEquals("Labrador", updatedAnimal.getBreed(), "Expected and actual breed should match");
        assertEquals(6, updatedAnimal.getAge(), "Expected and actual age should match");
        assertEquals("SPAYED", updatedAnimal.getMedicalHistory(), "Expected and actual medical history should match");
    }

    @Test
    void deleteAnimal() {
        // Make a DELETE request to delete the animal
        given()
                .when()
                .delete("/animals/" + createdAnimalId)
                .then()
                .statusCode(204);  // Expecting status code 204 No Content

        // Verify that the animal is no longer available
        given()
                .when()
                .get("/animals/" + createdAnimalId)
                .then()
                .statusCode(404);  // Expecting status code 404 Not Found
    }
}