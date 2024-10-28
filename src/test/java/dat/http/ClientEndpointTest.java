package dat.http;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class ClientEndpointTest extends BaseHttpTest {

    @Test
    public void testCreateClient() {
        String clientJson = """
        {
            "name": "John Doe",
            "email": "john@example.com",
            "phone": "1234567890",
            "address": "456 Elm St",
            "cityId": 1
        }""";

        given()
                .contentType(ContentType.JSON)
                .body(clientJson)
                .when()
                .post("/api/clients")
                .then()
                .statusCode(201)
                .body("name", equalTo("John Doe"))
                .body("email", equalTo("john@example.com"))
                .body("phone", equalTo("1234567890"))
                .body("id", notNullValue());
    }

    @Test
    public void testGetAllClients() {
        given()
                .when()
                .get("/api/clients")
                .then()
                .statusCode(200)
                .body("size()", equalTo(0));  // No clients initially
    }

    @Test
    public void testGetClientById() {
        // Create a new client
        String clientJson = """
        {
            "name": "Jane Doe",
            "email": "jane@example.com",
            "phone": "9876543210",
            "address": "789 Maple St",
            "cityId": 2
        }""";

        Integer clientId = given()
                .contentType(ContentType.JSON)
                .body(clientJson)
                .when()
                .post("/api/clients")
                .then()
                .statusCode(201)
                .extract().path("id");

        // Now get the client by ID
        given()
                .when()
                .get("/api/clients/" + clientId)
                .then()
                .statusCode(200)
                .body("name", equalTo("Jane Doe"))
                .body("email", equalTo("jane@example.com"));
    }

    @Test
    public void testUpdateClient() {
        // Create a client
        String clientJson = """
        {
            "name": "Sam Smith",
            "email": "sam@example.com",
            "phone": "1231231234",
            "address": "123 Oak St",
            "cityId": 1
        }""";

        Integer clientId = given()
                .contentType(ContentType.JSON)
                .body(clientJson)
                .when()
                .post("/api/clients")
                .then()
                .statusCode(201)
                .extract().path("id");

        // Update the client
        String updatedClientJson = """
        {
            "name": "Samuel Smith",
            "email": "samuel@example.com",
            "phone": "4321432143",
            "address": "456 Pine St",
            "cityId": 1
        }""";

        given()
                .contentType(ContentType.JSON)
                .body(updatedClientJson)
                .when()
                .put("/api/clients/" + clientId)
                .then()
                .statusCode(200)
                .body("name", equalTo("Samuel Smith"))
                .body("email", equalTo("samuel@example.com"));
    }

    @Test
    public void testDeleteClient() {
        // Create a client
        String clientJson = """
        {
            "name": "Client to Delete",
            "email": "delete@example.com",
            "phone": "987654321",
            "address": "456 Pine St",
            "cityId": 1
        }""";

        Integer clientId = given()
                .contentType(ContentType.JSON)
                .body(clientJson)
                .when()
                .post("/api/clients")
                .then()
                .statusCode(201)
                .extract().path("id");

        // Delete the client
        given()
                .when()
                .delete("/api/clients/" + clientId)
                .then()
                .statusCode(204);

        // Ensure the client no longer exists
        given()
                .when()
                .get("/api/clients/" + clientId)
                .then()
                .statusCode(404)
                .body("msg", equalTo("Client not found"));
    }
}
