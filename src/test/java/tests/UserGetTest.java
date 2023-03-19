package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Issue;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestKeys;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;

public class UserGetTest extends BaseTestKeys {

    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();
    @Test
    @Description("This 122test get data without auth")
    @DisplayName("This test get data without auth")
    @Issue("https://jira.home.work/browse/HB-33293")
    @Epic("2.6.0")
    public void testGetUserDataNotAuth(){
        Response responseUserData = RestAssured
                .get("https://playground.learnqa.ru/api/user/2")
                .andReturn();

        Assertions.assertJsonHasField(responseUserData, "username");
        Assertions.assertJsonHasNoField(responseUserData, "firsName");
        Assertions.assertJsonHasNoField(responseUserData, "lastName");
        Assertions.assertJsonHasNoField(responseUserData, "email");
    }

    @Test
    @Description("This test get details")
    @DisplayName("This test get details")
    @Issue("https://jira.home.work/browse/HB-33293")
    @Epic("2.6.0")
    public void testGetUserDetailsAuthAsSameUser() {
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = RestAssured
                .given()
                .body(authData)
                .when()
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();1

        String header = this.getHeader(responseGetAuth, "x-csrf-token");
        String cookie = this.getCookie(responseGetAuth, "auth_sid");

        Response responseUserData = RestAssured
                .given()
                .header("x-csrf-token",header)
                .cookie("auth_sid", cookie)
                .get("https://playground.learnqa.ru/api/user/2")
                .andReturn();

        String[] expectedFields = {"username", "firstName", "lastName", "email"};
        Assertions.assertJsonHasFields(responseUserData, expectedFields);
    }

    @Test
    @Description("This test tries to get details with different user")
    @DisplayName("This test get details")
    @Issue("https://jira.home.work/browse/HB-33293")
    @Epic("2.6.0")
    public void testGetUserDetailsAuthAsDifferentUser() {
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login",authData);

        String header = this.getHeader(responseGetAuth, "x-csrf-token");
        String cookie = this.getCookie(responseGetAuth, "auth_sid");

        Response responseUserData = apiCoreRequests
                .makeGetRequest("https://playground.learnqa.ru/api/user/1",
                        header,
                        cookie);

        Assertions.assertJsonHasField(responseUserData, "username");
        Assertions.assertJsonHasNoField(responseUserData, "firsName");
        Assertions.assertJsonHasNoField(responseUserData, "lastName");
        Assertions.assertJsonHasNoField(responseUserData, "email");
    }
}
