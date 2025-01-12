import models.*;
import org.junit.jupiter.api.Test;
import specs.Spec;

import static io.restassured.RestAssured.given;
import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;

public class ApiTests extends TestBase {

    @Test
    void createUserTest() {
        CreateUserRequest requestBody = new CreateUserRequest();
        requestBody.setName("morpheus");
        requestBody.setJob("leader");

        CreateUserResponse response = step("Create user", () ->
                given(Spec.requestSpec)
                        .body(requestBody)
                        .when()
                        .post("/users")
                        .then()
                        .spec(Spec.responseSpecification201)
                        .extract().as(CreateUserResponse.class)
        );

        step("Check response", () -> {
            assertThat(response).isNotNull();
            assertThat(response.getId()).isNotNull();
            assertThat(response.getName()).isEqualTo("morpheus");
            assertThat(response.getJob()).isEqualTo("leader");
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
            assertThat(response).isNotNull();
            assertThat(response.getPage()).isEqualTo(2);
            assertThat(response.getData()).isNotEmpty();
            User firstUser = response.getData().get(0);
            assertThat(firstUser.getEmail()).contains("@reqres.in");
        });
    }

    @Test
    void singleUserTest() {
        UserResponse response = step("Get user details", () ->
                given(Spec.requestSpec)
                        .when()
                        .get("/users/1")
                        .then()
                        .spec(Spec.responseSpecification200)
                        .extract().as(UserResponse.class)
        );

        step("Check user data", () -> {
            assertThat(response).isNotNull();
            User userData = response.getData();
            assertThat(userData).isNotNull();
            assertThat(userData.getId()).isEqualTo(1);
            assertThat(userData.getEmail()).isEqualTo("george.bluth@reqres.in");
            assertThat(userData.getFirstName()).isEqualTo("George");
            assertThat(userData.getLastName()).isEqualTo("Bluth");
            assertThat(userData.getAvatar()).isEqualTo("https://reqres.in/img/faces/1-image.jpg");

            Support support = response.getSupport();
            assertThat(support).isNotNull();
            assertThat(support.getUrl()).isNotEmpty();
            assertThat(support.getText()).contains("Content Caddy");
        });
    }

    @Test
    void singleUserNotFoundTest() {
        step("Get non-existing user", () ->
                given(Spec.requestSpec)
                        .when()
                        .get("/users/100000")
                        .then()
                        .spec(Spec.responseSpecification404)
                        .body(equalTo("{}"))
        );
    }

    @Test
    void registerSuccessfulTest() {
        RegisterRequest requestBody = new RegisterRequest();
        requestBody.setEmail("eve.holt@reqres.in");
        requestBody.setPassword("pistol");

        RegisterResponse response = step("Register user", () ->
                given(Spec.requestSpec)
                        .body(requestBody)
                        .when()
                        .post("/register")
                        .then()
                        .spec(Spec.responseSpecification200)
                        .extract().as(RegisterResponse.class)
        );

        step("Check response", () -> {
            assertThat(response).isNotNull();
            assertThat(response.getId()).isEqualTo(4);
            assertThat(response.getToken()).isNotNull();
        });
    }
}
