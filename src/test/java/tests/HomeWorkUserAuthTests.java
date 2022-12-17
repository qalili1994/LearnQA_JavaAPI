package tests;

import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import java.util.Map;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HomeWorkUserAuthTests {

    @Test
    public void getCookie() {
        Response responseGetCookies = given()
                .when()
                .get("https://playground.learnqa.ru/api/homework_cookie")
                .andReturn();


        Map<String, String> cookies = responseGetCookies.getCookies();

        System.out.println(responseGetCookies.getCookie("HomeWork"));
        assertTrue(cookies.containsKey("HomeWork"),"Response without 'HomeWork' cookie");
    }

    @Test
    public void getHeader() {
        Response responseGetHeader = given()
                .when()
                .get("https://playground.learnqa.ru/api/homework_header")
                .andReturn();

       // String header = responseGetHeader.getHeader("x-secret-homework-header");
        Headers headers = responseGetHeader.getHeaders();
        assertTrue(headers.hasHeaderWithName("x-secret-homework-header"), "Response without 'x-secret-homework-header' header");
    }
}
