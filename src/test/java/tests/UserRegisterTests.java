package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.Assertions;
import lib.DataGenerator;
import lib.BaseTestKeys;
import org.apache.groovy.util.Maps;
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

public class UserRegisterTests extends BaseTestKeys {

    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();
    private static Stream<Arguments> getNames() {
        return Stream.of(
                Arguments.of("firstName", null),
                Arguments.of("lastName", null)
        );
    }

    @Test
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
