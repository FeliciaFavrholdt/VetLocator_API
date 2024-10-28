package dat.http;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class SecurityTest extends BaseHttpTest {

    @Test
    public void testUnauthorizedAccess() {
        // Try to access a protected resource without proper authorization
        given()
                .when()
                .get("/api/clinics")
                .then()
                .statusCode(401)
                .body("msg", equalTo("Authentication failure, typically due to a missing or invalid API key."));
    }
}
