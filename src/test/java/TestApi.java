import models.Lombok;
import models.UserListResponse;
import org.junit.jupiter.api.Test;
import specs.Spec;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;


public class TestApi extends TestBase {

    @Test
    void createUserTest() {
        Lombok requestBody = new Lombok();
        requestBody.setName("morpheus");
        requestBody.setJob("leader");

        Lombok response = step("Create user", () ->
                given(Spec.requestSpec)
                        .body(requestBody)
                        .when()
                        .post("/users")
                        .then()
                        .spec(Spec.responseSpecification201)
                        .extract().as(Lombok.class)
        );

        step("Check response", () -> {
            assertNotNull(response.getId());
        });
    }
            @Test
        void listUserTest() {
            UserListResponse response = step("List users", () ->
                    given(Spec.requestSpec)
                            .queryParam("page", "2")
                            .when()
                            .get("/users")
                            .then()
                            .spec(Spec.responseSpecification200)
                            .extract().as(UserListResponse.class)
            );

            step("Validate response content", () -> {
                assertThat(response.getPage()).isEqualTo(2);
                assertThat(response.getData()).isNotEmpty();
                assertThat(response.getData().get(0).getEmail()).contains("@reqres.in");
            });
        }

    @Test
    void singleUserTest() {
        Lombok response = step("Get single user", () ->
                given(Spec.requestSpec)
                        .when()
                        .get("/users/1")
                        .then()
                        .spec(Spec.responseSpecification200)
                        .body("data.id", equalTo(1))
                        .extract().as(Lombok.class)
        );

        step("Check response", () -> {
            assertEquals(1, response.getData().getId());
        });
    }

    @Test
    void singleUserNotFoundTest() {
        Lombok response = step("Get non-existing user", () ->
                given(Spec.requestSpec)
                        .when()
                        .get("/users/100000")
                        .then()
                        .spec(Spec.responseSpecification404)
                        .body(equalTo("{}"))
                        .extract().as(Lombok.class)
        );

        step("Check response", () -> {
            assertNull(response.getData());
        });
    }

    @Test
    void registerSuccessfulTest() {
        Lombok requestBody = new Lombok();
        requestBody.setEmail("eve.holt@reqres.in");
        requestBody.setPassword("pistol");

        Lombok response = step("Register user", () ->
                given(Spec.requestSpec)
                        .body(requestBody)
                        .when()
                        .post("/register")
                        .then()
                        .spec(Spec.responseSpecification200)
                        .extract().as(Lombok.class)
        );

        step("Check response", () -> {
            assertEquals(4, Integer.parseInt(response.getId()));
            assertNotNull(response.getToken());
        });
    }
}
