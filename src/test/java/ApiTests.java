import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

public class ApiTests {

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "https://reqres.in";
        RestAssured.basePath = "/api";
    }


    @Test
    @DisplayName("Успешное создание пользователя")
    void successfulCreateUserTest() {
        String createData = "{\"name\": \"tata\", \"job\": \"QA\"}";

        given()
                .body(createData)
                .contentType(JSON)
                .log().uri()

        .when()
                .post("/users")

        .then()
                .log().status()
                .log().body()
                .statusCode(201)
                .body("name", is("tata"))
                .body("job", is("QA"))
                .body("id",  matchesPattern("^\\d{3}$"))
                .body("createdAt", notNullValue());

    }

    @Test
    @DisplayName("Проверка содержания 2-й страницы")
    void contentSecondPageTest() {

        given()
                .log().uri()

        .when()
                .get("/users?page=2")

        .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("page", equalTo(2))
                .body("per_page", equalTo(6))
                .body("total", equalTo(12))
                .body("total_pages", equalTo(2))
                .body("data", hasSize(6))
                .body("support.url", equalTo("https://contentcaddy.io?utm_source=reqres&utm_medium=json&utm_campaign=referral"))
                .body("support.text", equalTo("Tired of writing endless social media content? Let Content Caddy generate it for you."));

    }

    @Test
    @DisplayName("Успешное удаление пользователя")
    void successfulDeleteUserTest() {

        given()
                .log().uri()

        .when()
                .delete("/users/2")

        .then()
                .log().status()
                .log().body()
                .statusCode(204)
                .body(emptyOrNullString());

    }

    @Test
    @DisplayName("Запрос на существующего  юзера")
    void singleExistingUserTest() {

        given()
                .log().uri()

        .when()
                .get("/unknown/2")

        .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("data.id", equalTo(2))
                .body("data.name", equalTo("fuchsia rose"))
                .body("data.year", equalTo(2001))
                .body("data.color", equalTo("#C74375"))
                .body("data.pantone_value", equalTo("17-2031"));

    }

    @Test
    @DisplayName("Запрос на несуществующего  юзера")
    void singleNonExistingUserTest() {

        given()
                .log().uri()

        .when()
                .get("/unknown/22")

        .then()
                .log().status()
                .log().body()
                .statusCode(404)
                .body(equalTo("{}"));

    }
}
