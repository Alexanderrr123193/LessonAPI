import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class TestApi {

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "https://reqres.in";
        RestAssured.basePath = "/api";
    }

    @Test
    void createUserTest() {
        given()
                .contentType(ContentType.JSON)
                .body("{" +
                        "\"name\": \"morpheus\"," +
                        "\"job\": \"leader\"" +
                        "}")
                .when()
                .post("/users")
                .then()
                .statusCode(201)
                .body("id", notNullValue());
    }

    @Test
    void listUserTest() {
        given()
                .queryParam("page", "2")
                .when()
                .get("/users")
                .then()
                .statusCode(200)
                .body("page", equalTo(2));
    }

    @Test
    void singleUserTest() {
        given()
                .when()
                .get("/user/1")
                .then()
                .statusCode(200)
                .body("data.id", equalTo(1));
    }

    @Test
    void singleUserNotFoundTest() {
        given()
                .when()
                .get("/user/100000")
                .then()
                .statusCode(404)
                .body(equalTo("{}"));
    }

    @Test
    void registerSuccessfulTest() {
        given()
                .contentType(ContentType.JSON)
                .body("{" +
                        "\"email\": \"eve.holt@reqres.in\"," +
                        "\"password\": \"pistol\"" +
                        "}")
                .when()
                .post("/register")
                .then()
                .statusCode(200)
                .body("id", equalTo(4))
                .body("token", notNullValue());
    }
}
