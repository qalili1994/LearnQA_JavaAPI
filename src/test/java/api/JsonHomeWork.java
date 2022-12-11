package api;

import api.models.Data;
import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import java.util.List;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.Matchers.notNullValue;


public class JsonHomeWork {
    private final static String URL = "https://playground.learnqa.ru";

    @Test
    @Description("Вытаскиваем второй месседж")
    public void testGetJsonHomeWork() {
        List<Data> messages = given()
                .when()
                .get(URL + "/api/get_json_homework")
                .then().extract().body().jsonPath().getList("messages", Data.class);

        System.out.println(messages.get(1).getMessage());
    }

    @Test
    @Description("Вытаскиваем location")
    public void testLongRedirect() {
        Response response = RestAssured
                .given()
                .redirects()
                .follow(false)
                .when()
                .get(URL + "/api/long_redirect")
                .andReturn();

        String locationHeader = response.getHeader("Location");
        System.out.println(locationHeader);
    }

    @Test
    @Description("Считаем все редиректы")
    public void testLongRedicts() {
        String url = "https://playground.learnqa.ru/api/long_redirect";
        int count = 0;

        Response response;
        do {
            response = RestAssured
                    .given()
                    .redirects()
                    .follow(false)
                    .when()
                    .get(url)
                    .thenReturn();
            url = response.getHeader("Location");
            count++;
        } while (response.statusCode() != 200);

        System.out.println(count - 1);
    }

    @Test
    @Description("Запрос c token ПОСЛЕ того, как задача готова, " +
                    "убеждался в правильности поля status и наличии поля result")

    public void longTimeJob() throws InterruptedException {
        step("Получаем токен и время через сколько задача будет выполнена");
        JsonPath response = RestAssured
                .given()
                .when()
                .get(URL + "/ajax/api/longtime_job")
                .jsonPath();

        String token = response.get("token");
        Integer seconds = response.get("seconds");

        step("Проверим статус до того, как задача готова");
        Response response1 = RestAssured
                .given()
                .param("token", token)
                .when()
                .get(URL + "/ajax/api/longtime_job")
                .then()
                .body("status", equalTo("Job is NOT ready"))
                .extract().response();

        step("Проверим статус и результат, как задача готова");
        Thread.sleep(seconds * 1000);
        JsonPath response12 = RestAssured
                .given()
                .param("token", token)
                .when()
                .get(URL + "/ajax/api/longtime_job")
                .then()
                .body("result", notNullValue())
                .body("status", equalTo("Job is ready"))
                .extract().response()
                .jsonPath();
    }
}












