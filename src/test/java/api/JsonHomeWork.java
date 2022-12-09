package api;

import api.models.Data;
import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import io.restassured.http.Headers;

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
}



