package specs;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.with;
import static io.restassured.filter.log.LogDetail.BODY;
import static io.restassured.filter.log.LogDetail.STATUS;

public class Spec {

    public static RequestSpecification requestSpec = with()
            .contentType(ContentType.JSON)
            .filter(new AllureRestAssured())
            .log().uri()
            .log().body()
            .log().headers();

    public static ResponseSpecification createResponseSpecification(int statusCode, boolean expectJson) {
        ResponseSpecBuilder builder = new ResponseSpecBuilder()
                .expectStatusCode(statusCode)
                .log(STATUS)
                .log(BODY);

        if (expectJson) {
            builder.expectContentType(ContentType.JSON);
        }

        return builder.build();
    }

    public static final ResponseSpecification responseSpecification200 = createResponseSpecification(200, true);
    public static final ResponseSpecification responseSpecification201 = createResponseSpecification(201, true);
    public static final ResponseSpecification responseSpecification404 = createResponseSpecification(404, false);
    public static final ResponseSpecification responseSpecification400 = createResponseSpecification(400, false);
}
