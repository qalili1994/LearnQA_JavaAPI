package api;

import api.models.Data;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;


public class JsonHomeWork {
    private final static String URL = "https://playground.learnqa.ru";

    @Test
    public void testGetJsonHomeWork() {
        List<Data> messages = given()
                .when()
                .get(URL + "/api/get_json_homework")
                .then().extract().body().jsonPath().getList("messages", Data.class);

        System.out.println(messages.get(1).getMessage());
    }

    @Test
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

}














