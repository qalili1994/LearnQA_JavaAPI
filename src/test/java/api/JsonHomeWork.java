package api;

import api.models.Data;
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
}
