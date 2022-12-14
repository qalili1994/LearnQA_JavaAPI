package lib;

import io.restassured.response.Response;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.*;

public class Assertions {
    public static  void assertJsonHasField(Response Response, String expectedFieldName){
        Response.then().assertThat().body("$", hasKey(expectedFieldName));
    }

    public static void assertJsonHasFields(Response Response, String[] expectedFieldNames){
        for (String exprectedFieldName : expectedFieldNames){
            Assertions.assertJsonHasField(Response, exprectedFieldName);
        }
    }

    public static  void assertJsonHasNoField(Response Response, String unexpectedFieldName){
        Response.then().assertThat().body("$", not(hasKey(unexpectedFieldName)));
    }

    public static void asserJsonByName(Response Response, String name, int expectedValue) {
        Response.then().assertThat().body("$",hasKey(name));

        int value = Response.jsonPath().getInt(name);
        assertEquals(expectedValue, value, "JSON value isnt equal to expected value");
    }

    public static void asserJsonByName(Response Response, String name, String expectedValue) {
        Response.then().assertThat().body("$",hasKey(name));

        String value = Response.jsonPath().getString(name);
        assertEquals(expectedValue, value, "JSON value isnt equal to expected value");
    }

    public static void assertResponseTextEquals(Response Response, String expectedAnswer){
        assertEquals(
                expectedAnswer,
                Response.asString(),
                "Response text is not as expected"
        );
    }

    public static void assertResponseTextNotEquals(Response Response, String expectedAnswer){
        assertNotEquals(
                expectedAnswer,
                Response.asString(),
                "Response text is the same"
        );
    }

    public static void assertResponseFieldNotEquals(Response Response,String name, String expectedValue){
        Response.then().assertThat().body("$",hasKey(name));

        String value = Response.jsonPath().getString(name);

        assertNotEquals(expectedValue, value, "Response fields are the same");
    }

    public static void assertResponseStatusCode(Response response, int expectedStatusCode) {
        assertEquals(
                expectedStatusCode,
                response.statusCode(),
                "Response status code is not as expected"
        );

    }
}
