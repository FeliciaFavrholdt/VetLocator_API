package dat.http;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class AnimalEndpointTest extends BaseHttpTest {

    @Test
    public void testCreateAnimal() {
        // Define JSON body for creating an Animal
        String animalJson = """
        {
            "name": "Buddy",
            "species": "Dog",
            "age": 5,
            "ownerId": 1  // Assuming the owner with ID 1 exists
        }""";

        // Send POST request to create the Animal
        given()
                .contentType(ContentType.JSON)
                .body(animalJson)
                .when()
                .post("/api/animals")
                .then()
                .statusCode(201)
                .body("name", equalTo("Buddy"))
                .body("species", equalTo("Dog"))
                .body("age", equalTo(5))
                .body("id", notNullValue());
    }

    @Test
    public void testGetAllAnimals() {
        given()
                .when()
                .get("/api/animals")
                .then()
                .statusCode(200)
                .body("size()", equalTo(0));  // No animals present initially
    }

    @Test
    public void testGetAnimalById() {
        // Create a new animal
        String animalJson = """
        {
            "name": "Whiskers",
            "species": "Cat",
            "age": 3,
            "ownerId": 1  // Assuming the owner with ID 1 exists
        }""";

        Integer animalId = given()
                .contentType(ContentType.JSON)
                .body(animalJson)
                .when()
                .post("/api/animals")
                .then()
                .statusCode(201)
                .extract().path("id");

        // Now get the animal by ID
        given()
                .when()
                .get("/api/animals/" + animalId)
                .then()
                .statusCode(200)
                .body("name", equalTo("Whiskers"))
                .body("species", equalTo("Cat"))
                .body("age", equalTo(3));
    }

    @Test
    public void testAnimalNotFound() {
        // Try to get an animal that doesn't exist
        given()
                .when()
                .get("/api/animals/9999")  // Non-existent animal ID
                .then()
                .statusCode(404)
                .body("msg", equalTo("Animal not found"));
    }

    @Test
    public void testCreateAnimalWithMissingFields() {
        // Define JSON body with missing required fields
        String incompleteAnimalJson = """
        {
            "species": "Dog"
        }""";

        given()
                .contentType(ContentType.JSON)
                .body(incompleteAnimalJson)
                .when()
                .post("/api/animals")
                .then()
                .statusCode(400)
                .body("msg", equalTo("Invalid or missing parameters in the animal entity"));
    }

    @Test
    public void testUpdateAnimal() {
        // Create an animal first
        String animalJson = """
        {
            "name": "Luna",
            "species": "Rabbit",
            "age": 2,
            "ownerId": 1  // Assuming the owner with ID 1 exists
        }""";

        Integer animalId = given()
                .contentType(ContentType.JSON)
                .body(animalJson)
                .when()
                .post("/api/animals")
                .then()
                .statusCode(201)
                .extract().path("id");

        // Update the animal details
        String updatedAnimalJson = """
        {
            "name": "Luna",
            "species": "Rabbit",
            "age": 3,
            "ownerId": 1
        }""";

        given()
                .contentType(ContentType.JSON)
                .body(updatedAnimalJson)
                .when()
                .put("/api/animals/" + animalId)
                .then()
                .statusCode(200)
                .body("age", equalTo(3));  // Check that the age was updated
    }

    @Test
    public void testDeleteAnimal() {
        // Create an animal to delete
        String animalJson = """
        {
            "name": "Max",
            "species": "Dog",
            "age": 4,
            "ownerId": 1  // Assuming the owner with ID 1 exists
        }""";

        Integer animalId = given()
                .contentType(ContentType.JSON)
                .body(animalJson)
                .when()
                .post("/api/animals")
                .then()
                .statusCode(201)
                .extract().path("id");

        // Delete the animal
        given()
                .when()
                .delete("/api/animals/" + animalId)
                .then()
                .statusCode(204);  // HTTP No Content

        // Ensure the animal no longer exists
        given()
                .when()
                .get("/api/animals/" + animalId)
                .then()
                .statusCode(404)
                .body("msg", equalTo("Animal not found"));
    }
}