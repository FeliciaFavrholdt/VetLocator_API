package dat.http;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class ClinicEndpointTest extends dat.integration.BaseHttpTest {

    @Test
    public void testGetAllClinics() {
        given()
                .when()
                .get("/api/clinics")
                .then()
                .statusCode(200)  // OK status
                .body("size()", equalTo(0));  // Assuming no clinics are initially present
    }

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
                .statusCode(201)  // Created
                .body("name", equalTo("Test Clinic"));
    }
}