import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class AuthIntegrationTest {
    @BeforeAll
    static void setup() {
        //auth uri
        RestAssured.baseURI = "http://localhost:4004";
    }

    @Test
    public void shouldReturnOKWithValidToken() {
        // 1. Arrange
        // 2. Act
        // 3. Assert

        // define property that is going to send in request(test)
        String loginPayload = """
                    {
                        "email": "testuser@test.com",
                        "password": "password123"
                    }
                """;
        Response response = given()
                .contentType("application/json")
                .body(loginPayload) //set up
                .when()
                .post("/auth/login") // post req
                .then()
                .statusCode(200)
                .body("token", notNullValue()) // check 200
                .extract()
                .response();

        System.out.println("Generated token: " + response.jsonPath().getString("token"));
    }

    @Test
    public void shouldReturnUnauthorizedWithInvalidLogin() {
        // define property that is going to send in request(test)
        String loginPayload = """
                    {
                        "email": "invalid_user@test.com",
                        "password": "wrongpassword"
                    }
                """;
        given()
                .contentType("application/json")
                .body(loginPayload) //set up
                .when()
                .post("/auth/login") // post req
                .then()
                .statusCode(401); //check 401
    }
}
