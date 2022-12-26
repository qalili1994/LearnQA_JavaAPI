package tests;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTestKeys;
import lib.DataGenerator;
import lib.ApiCoreRequests;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static lib.DataGenerator.shortName;

public class UserDeleteTest extends BaseTestKeys {

    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();
    Map<String, String> userData = DataGenerator.getRegistrationData();

    @Test
    public void testDeleteUserId2() {
        //login
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        //delete
        Response responseDelete = apiCoreRequests
                .makeDeleteRequestField(
                        "https://playground.learnqa.ru/api/user/2",
                        this.getHeader(responseGetAuth, "x-csrf-token"),
                        this.getCookie(responseGetAuth, "auth_sid"));

        Assertions.assertResponseStatusCode(responseDelete, 400);
        Assertions.assertResponseTextEquals(responseDelete, "Please, do not delete test users with ID 1, 2, 3, 4 or 5.");
    }

    @Test
    public void testDeleteUser() {
        //generate user
        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);

        JsonPath jsonPath = responseCreateAuth.jsonPath();
        String userId = jsonPath.get("id");

        //login
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        //delete
        Response responseDelete = apiCoreRequests
                .makeDeleteRequestField(
                        "https://playground.learnqa.ru/api/user/" + userId,
                        this.getHeader(responseGetAuth, "x-csrf-token"),
                        this.getCookie(responseGetAuth, "auth_sid"));

        //get
        Response responseUserData = apiCoreRequests
                .makeGetRequest(
                        "https://playground.learnqa.ru/api/user/" + userId,
                        this.getHeader(responseGetAuth, "x-csrf-token"),
                        this.getCookie(responseGetAuth, "auth_sid"));

        Assertions.assertResponseStatusCode(responseUserData, 404);
        Assertions.assertResponseTextEquals(responseUserData, "User not found");
    }

    @Test
    public void testDeleteOtherUser() {
        //generate user
        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);

        JsonPath jsonPath = responseCreateAuth.jsonPath();

        //login
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        //delete
        Response responseDelete = apiCoreRequests
                .makeDeleteRequestField(
                        "https://playground.learnqa.ru/api/user/555",
                        this.getHeader(responseGetAuth, "x-csrf-token"),
                        this.getCookie(responseGetAuth, "auth_sid"));

        //get
        Response responseUserData = apiCoreRequests
                .makeGetRequest(
                        "https://playground.learnqa.ru/api/user/555",
                        this.getHeader(responseGetAuth, "x-csrf-token"),
                        this.getCookie(responseGetAuth, "auth_sid"));

        Assertions.assertResponseTextNotEquals(responseUserData, "User not found");
    }
}