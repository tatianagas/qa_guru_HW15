package tests;

import models.CreateRequestModel;
import models.CreateResponseModel;
import models.Page2ResponseModel;
import models.UserResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.TestSpec.*;


@Tag("api")
public class ApiTests extends TestBase {


    @Test
    @DisplayName("Успешное создание пользователя")
    void successfulCreateUserTest() {
        CreateRequestModel createData = new CreateRequestModel();
        createData.setName("tata");
        createData.setJob("QA");

        CreateResponseModel response = step("Создаем пользователя, вводим его имя и должность", () ->
                given(requestSpec)
                        .body(createData)

                        .when()
                        .post("/users")

                        .then()
                        .spec(responseCod201Spec)
                        .extract().as(CreateResponseModel.class));


        step("Проверяем создание пользователя", () -> {
            assertThat(response.getName()).isEqualTo("tata");
            assertThat(response.getJob()).isEqualTo("QA");
            assertThat(response.getId()).matches("^\\d{3}$");
            assertThat(response.getCreatedAt()).isNotNull();
        });

    }

    @Test
    @DisplayName("Проверка содержания 2-й страницы")
    void contentSecondPageTest() {

        Page2ResponseModel response = step("Создаем пользователя, вводим его имя и должность", () ->
                given(requestSpec)

                        .when()
                        .get("/users?page=2")

                        .then()
                        .spec(responseCod200Spec)
                        .extract().as(Page2ResponseModel.class));


        step("Проверяем содержимое страницы", () -> {
            assertThat(response.getPage()).isEqualTo("2");
            assertThat(response.getPer_page()).isEqualTo("6");
            assertThat(response.getTotal()).isEqualTo("12");
            assertThat(response.getTotal_pages()).isEqualTo("2");
            assertThat(response.getData()).hasSize(6);
            assertThat(response.getSupport().getUrl())
                    .isEqualTo("https://contentcaddy.io?utm_source=reqres&utm_medium=json&utm_campaign=referral");
            assertThat(response.getSupport().getText())
                    .isEqualTo("Tired of writing endless social media content? Let Content Caddy generate it for you.");
        });

    }

    @Test
    @DisplayName("Успешное удаление пользователя")
    void successfulDeleteUserTest() {

        step("Удаляем пользователя с ID 2", () -> {
            given(requestSpec)

                    .when()
                    .delete("/users/2")

                    .then()
                    .spec(responseCod204Spec);
        });

    }

    @Test
    @DisplayName("Запрос на существующего  юзера")
    void singleExistingUserTest() {

        UserResponseModel response = step("Проверяем пользователя с ID 2", () ->
                given(requestSpec)

                        .when()
                        .get("/unknown/2")

                        .then()
                        .spec(responseCod200Spec)
                        .extract().as(UserResponseModel.class));

        step("Проверяем свойства пользователя с ID 2", () -> {
            assertThat(response.getData().getId()).isEqualTo("2");
            assertThat(response.getData().getName()).isEqualTo("fuchsia rose");
            assertThat(response.getData().getYear()).isEqualTo("2001");
            assertThat(response.getData().getColor()).isEqualTo("#C74375");
            assertThat(response.getData().getPantone_value()).isEqualTo("17-2031");
        });
    }

    @Test
    @DisplayName("Запрос на несуществующего  юзера")
    void singleNonExistingUserTest() {

        UserResponseModel response = step("Проверяем пользователя с ID 22", () ->
                given(requestSpec)

                        .when()
                        .get("/unknown/22")

                        .then()
                        .spec(responseCod404Spec)
                        .extract().as(UserResponseModel.class));

        step("Проверяем, что тело ответа пустое", () -> {
            assertThat(response.getData()).isNull();
        });

    }
}
