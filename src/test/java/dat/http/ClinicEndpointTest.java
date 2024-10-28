package dat.http;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ClinicEndpointTest extends BaseHttpTest {

    @Test
    public void testCreateClinic() {
        String clinicJson = """
        {
            "name": "Test Clinic",
            "address": "123 Main St",
            "contactPhone": "1234567890",
            "emergencyServices": true,
            "cityId": 1
        }""";

        given()
                .contentType(ContentType.JSON)
                .body(clinicJson)
                .when()
                .post("/api/clinics")
                .then()
                .statusCode(201)
                .body("name", equalTo("Test Clinic"))
                .body("address", equalTo("123 Main St"))
                .body("contactPhone", equalTo("1234567890"))
                .body("emergencyServices", equalTo(true));
    }

    @Test
    public void testGetAllClinics() {
        given()
                .when()
                .get("/api/clinics")
                .then()
                .statusCode(200)
                .body("size()", equalTo(0));  // Initially, no clinics present
    }

    @Test
    public void testGetClinicById() {
        // Create a new clinic
        String clinicJson = """
        {
            "name": "Another Clinic",
            "address": "456 Elm St",
            "contactPhone": "9876543210",
            "emergencyServices": false,
            "cityId": 2
        }""";

        Integer clinicId = given()
                .contentType(ContentType.JSON)
                .body(clinicJson)
                .when()
                .post("/api/clinics")
                .then()
                .statusCode(201)
                .extract().path("id");

        // Now get the clinic by ID
        given()
                .when()
                .get("/api/clinics/" + clinicId)
                .then()
                .statusCode(200)
                .body("name", equalTo("Another Clinic"))
                .body("address", equalTo("456 Elm St"))
                .body("emergencyServices", equalTo(false));
    }

    @Test
    public void testClinicNotFound() {
        given()
                .when()
                .get("/api/clinics/9999")  // Non-existent clinic ID
                .then()
                .statusCode(404)
                .body("msg", equalTo("Clinic not found"));
    }

    @Test
    public void testInvalidClinicCreate() {
        String invalidClinicJson = """
        {
            "address": "No Name"
        }""";

        given()
                .contentType(ContentType.JSON)
                .body(invalidClinicJson)
                .when()
                .post("/api/clinics")
                .then()
                .statusCode(400)
                .body("msg", equalTo("Invalid or missing parameters in the request."));
    }

    @Test
    public void testUpdateClinic() {
        String clinicJson = """
        {
            "name": "Clinic to Update",
            "address": "789 Oak St",
            "contactPhone": "5551234567",
            "emergencyServices": true,
            "cityId": 1
        }""";

        Integer clinicId = given()
                .contentType(ContentType.JSON)
                .body(clinicJson)
                .when()
                .post("/api/clinics")
                .then()
                .statusCode(201)
                .extract().path("id");

        String updatedClinicJson = """
        {
            "name": "Updated Clinic",
            "address": "987 Maple St",
            "contactPhone": "5559876543",
            "emergencyServices": false,
            "cityId": 1
        }""";

        given()
                .contentType(ContentType.JSON)
                .body(updatedClinicJson)
                .when()
                .put("/api/clinics/" + clinicId)
                .then()
                .statusCode(200)
                .body("name", equalTo("Updated Clinic"))
                .body("address", equalTo("987 Maple St"))
                .body("contactPhone", equalTo("5559876543"))
                .body("emergencyServices", equalTo(false));
    }

    @Test
    public void testDeleteClinic() {
        String clinicJson = """
        {
            "name": "Clinic to Delete",
            "address": "123 Pine St",
            "contactPhone": "5559876543",
            "emergencyServices": true,
            "cityId": 1
        }""";

        Integer clinicId = given()
                .contentType(ContentType.JSON)
                .body(clinicJson)
                .when()
                .post("/api/clinics")
                .then()
                .statusCode(201)
                .extract().path("id");

        // Delete the clinic
        given()
                .when()
                .delete("/api/clinics/" + clinicId)
                .then()
                .statusCode(204);

        // Ensure the clinic no longer exists
        given()
                .when()
                .get("/api/clinics/" + clinicId)
                .then()
                .statusCode(404)
                .body("msg", equalTo("Clinic not found"));
    }
}