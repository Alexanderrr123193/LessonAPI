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
            assertThat(response.getPage()).isEqualTo(2);
            assertThat(response.getData()).isNotEmpty();
            assertThat(response.getData().get(0).getEmail()).contains("@reqres.in");
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
                        .extract().as(UserResponse.class) // Десериализуем в UserResponse
        );

        step("Check user data", () -> {
            assertThat(response.getData().getId()).isEqualTo(1);
            assertThat(response.getData().getEmail()).isEqualTo("george.bluth@reqres.in");
            assertThat(response.getData().getFirst_name()).isEqualTo("George");
            assertThat(response.getData().getLast_name()).isEqualTo("Bluth");
            assertThat(response.getData().getAvatar()).isEqualTo("https://reqres.in/img/faces/1-image.jpg");

            assertThat(response.getSupport().getUrl()).isNotEmpty();
            assertThat(response.getSupport().getText()).contains("Content Caddy");
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
            assertThat(response.getId()).isEqualTo(4); // Сравниваем с числом
            assertThat(response.getToken()).isNotNull();
        });
    }

}
