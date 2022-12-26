package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Issue;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTestKeys;
import lib.DataGenerator;
import lib.ApiCoreRequests;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static lib.DataGenerator.shortName;

public class UserEditTest extends BaseTestKeys {

    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();
    String cookie;
    String header;
    Map<String, String> body = new HashMap<>();
    Map<String, String> userData = DataGenerator.getRegistrationData();

    @Test
    @Description("This test edit user")
    @DisplayName("Test edit user")
    @Issue("https://jira.home.work/browse/HB-332293")
    @Epic("2.6.0")
    public void testEditJustCreatedTest() {
        //generate user

        JsonPath responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .jsonPath();

        String userId = responseCreateAuth.getString("id");

        //login
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();

        //edit
        String newName = "Changed Name";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditUser = RestAssured
                .given()
                .header("x-csrf-token", this.getHeader(responseGetAuth, "x-csrf-token"))
                .cookie("auth_sid", this.getCookie(responseGetAuth, "auth_sid"))
                .body(editData)
                .put("https://playground.learnqa.ru/api/user/" + userId)
                .andReturn();

        //get
        Response responseUserData = RestAssured
                .given()
                .header("x-csrf-token", this.getHeader(responseGetAuth, "x-csrf-token"))
                .cookie("auth_sid", this.getCookie(responseGetAuth, "auth_sid"))
                .get("https://playground.learnqa.ru/api/user/" + userId)
                .andReturn();

        Assertions.asserJsonByName(responseUserData, "firstName", newName);
    }

    @Test
    @Description("This test tries to edit user unathourized")
    @DisplayName("Test tries to edit")
    @Issue("https://jira.home.work/browse/HB-33293")
    @Epic("2.6.0")
    public void testEditUnauthorized() {

        Response responseEditUser = apiCoreRequests.
                makePutRequestEdit(
                        "https://playground.learnqa.ru/api/user/2",
                        header,
                        cookie,
                        body);

        Assertions.assertResponseStatusCode(responseEditUser, 400);
        Assertions.assertResponseTextEquals(responseEditUser, "Auth token not supplied");
    }

    @Test
    @Description("This test tries to edit other user")
    @DisplayName("Test tries to delete other user")
    @Issue("https://jira.home.work/browse/HB-33293")
    @Epic("2.6.0")
    public void testEditAsDifferentUser() {
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

        //edit
        String newName = "Changed Name";
        Map<String, String> editData = new HashMap<>();
        editData.put("username", newName);

        Response responseEditUser = apiCoreRequests
                .makePutRequestEdit(
                        "https://playground.learnqa.ru/api/user/2",
                        this.getHeader(responseGetAuth, "x-csrf-token"),
                        this.getCookie(responseGetAuth, "auth_sid"),
                        editData);

        //get
        Response responseUserData = apiCoreRequests
                .makeGetRequest(
                        "https://playground.learnqa.ru/api/user/2",
                        this.getHeader(responseGetAuth, "x-csrf-token"),
                        this.getCookie(responseGetAuth, "auth_sid"));

        Assertions.assertResponseStatusCode(responseEditUser, 200);
        Assertions.assertResponseFieldNotEquals(responseUserData, "username", newName);
    }

    @Test
    @Description("This test edit email without @")
    @DisplayName("Test edit mail whothut @")
    @Issue("https://jira.home.work/browse/HB-33293")
    @Epic("2.6.0")
    public void testEditMailWithoutAt() {
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

        //edit
        String mail = "Changedmail.ru";
        Map<String, String> editData = new HashMap<>();
        editData.put("email", mail);

        Response responseEditUser = apiCoreRequests
                .makePutRequestEdit(
                        "https://playground.learnqa.ru/api/user/" + userId,
                        this.getHeader(responseGetAuth, "x-csrf-token"),
                        this.getCookie(responseGetAuth, "auth_sid"),
                        editData);

        Assertions.assertResponseStatusCode(responseEditUser, 400);
        Assertions.assertResponseTextEquals(responseEditUser, "Invalid email format");
    }

    @Test
    @Description("This test edit name")
    @DisplayName("This test edit name")
    @Issue("https://jira.home.work/browse/HB-33293")
    @Epic("2.6.0")
    public void testEditFirstNameShort() {
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

        //edit
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", shortName);

        Response responseEditUser = apiCoreRequests
                .makePutRequestEdit(
                        "https://playground.learnqa.ru/api/user/" + userId,
                        this.getHeader(responseGetAuth, "x-csrf-token"),
                        this.getCookie(responseGetAuth, "auth_sid"),
                        editData);

        Assertions.assertResponseStatusCode(responseEditUser, 400);
        Assertions.asserJsonByName(responseEditUser, "error", "Too short value for field firstName");

    }
}