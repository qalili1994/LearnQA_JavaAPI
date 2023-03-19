package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Issue;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.Assertions;
import lib.DataGenerator;
import lib.BaseTestKeys;
import org.apache.groovy.util.Maps;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import lib.ApiCoreRequests;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertAll;

public class UserRegisterTests extends BaseTestKeys {

    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();
    private static Stream<Arguments> getNames() {
        return Stream.of(
                Arguments.of("firstName", null),
                Arguments.of("lastName", null)
        );
    }

    @Test
    @Description("This test create with user already exist")
    @DisplayName("This test create with user already exisst")
    @Issue("https://jira.home.work/browse/HB-33293")
    @Epic("2.6.0")
    public void testCreateUserWithExistingEmail() {
        String email = "vinkotov@example.com";

        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .andReturn();

        Assertions.assertResponseStatusCode(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "Users with email '" + email + "' already exists");
    }

    @Test
    @Description("This test create user")
    @DisplayName("This test create user")
    @Issue("https://jira.home.work/browse/HB-33293")
    @Epic("2.6.0")
    public void testCreateUserSuccessfully() {
        Map<String, String> userData = DataGenerator.getRegistrationData();

        Response responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .andReturn();

        Assertions.assertResponseStatusCode(responseCreateAuth, 200);
        Assertions.assertJsonHasField(responseCreateAuth, "id");
    }

    @Test
    @Description("This test create user without  @")
    @DisplayName("This test create user without @")
    @Issue("https://jira.home.work/browse/HB-33293")
    @Epic("2.6.0")
    public void testCreateUserWithoutAt() {
        String email = "vinkotovexample.com";

        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreate = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user", userData);

        Assertions.assertResponseStatusCode(responseCreate, 400);
        Assertions.assertResponseTextEquals(responseCreate, "Invalid email format");
    }

    @ParameterizedTest
    @MethodSource("getNames")
    @Description("This test create user without one field")
    @DisplayName("This test create user without one field")
    @Issue("https://jira.home.work/browse/HB-33293")
    @Epic("2.6.0")
    public void testCreateUserWithoutOneField(String params, String name) {

        Map<String, String> userData = new HashMap<>();
        userData.put(params, name);
        userData = DataGenerator.getRegistrationDataDelete(userData);

        Response responseCreate = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user", userData);

        Assertions.assertResponseStatusCode(responseCreate, 400);
        Assertions.assertResponseTextEquals(responseCreate, "The following required params are missed: " + params);
    }

    @ParameterizedTest
    @MethodSource("getNames")
    @Description("This test create user without one param")
    @DisplayName("This test create user without one param")
    @Issue("https://jira.home.work/browse/HB-33293")
    @Epic("2.6.0")
    public void testCreateUserWithoutParam(String params, String name) {

        Map<String, String> userData = new HashMap<>();
        userData.put(params, name);
        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreate = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user", userData);

        Assertions.assertResponseStatusCode(responseCreate, 400);
        Assertions.assertResponseTextEquals(responseCreate, "The following required params are missed: " + params);
    }

    @Test
    @Description("This test create user with short name")
    @DisplayName("This test create with short name")
    @Issue("https://jira.home.work/browse/HB-33293")
    @Epic("2.6.0")
    public void testCreateUserWithShortName() {
        Map<String, String> userData = new HashMap<>();
        userData.put("firstName", DataGenerator.shortName);
        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreate = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user", userData);


        Assertions.assertResponseStatusCode(responseCreate, 400);
        Assertions.assertResponseTextEquals(responseCreate, "The value of 'firstName' field is too short");
    }

    @Test
    @Description("This test create user with long name")
    @DisplayName("This test create user with long name")
    @Issue("https://jira.home.work/browse/HB-33293")
    @Epic("2.6.0")
    public void testCreateUserWithLongName() {
        Map<String, String> userData = new HashMap<>();
        userData.put("firstName", DataGenerator.longName);
        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreate = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user", userData);

        Assertions.assertResponseStatusCode(responseCreate, 400);
        Assertions.assertResponseTextEquals(responseCreate, "The value of 'firstName' field is too long");
    }
}
