package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lib.BaseTestKeys;
import lib.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import java.util.HashMap;
import java.util.Map;
import static io.restassured.RestAssured.given;


    public class UserAuthTests extends BaseTestKeys {

        String cookie;
        String header;
        int userIdOnAuth;

        @BeforeEach
        public void loginUser() {
            Map<String, String> authData = new HashMap<>();
            authData.put("email", "vinkotov@example.com");
            authData.put("password", "1234");

            Response responseGetAuth = given()
                    .body(authData)
                    .when()
                    .post("https://playground.learnqa.ru/api/user/login")
                    .prettyPeek()
                    .andReturn();

            this.cookie = this.getCookie(responseGetAuth, "auth_sid");
            this.header = this.getHeader(responseGetAuth,"x-csrf-token");
            this.userIdOnAuth = this.getIntFromJson(responseGetAuth,"user_id");
        }

        @Test
        public void testAuthUser() {

            Response responseCheckAuth = RestAssured
                    .given()
                    .header("x-csrf-token", this.header)
                    .cookie("auth_sid", this.cookie)
                    .get("https://playground.learnqa.ru/api/user/auth")
                    .andReturn();

            Assertions.asserJsonByName(responseCheckAuth, "user_id", this.userIdOnAuth);
        }

        @ParameterizedTest
        @ValueSource(strings = {"cookie", "headers"})
        public void testNegativeAuthUser(String condition) {
            RequestSpecification spec = RestAssured.given();
            spec.baseUri("https://playground.learnqa.ru/api/user/auth");

            if (condition.equals("cookie")) {
                spec.cookie("auth_sid", this.cookie);
            } else if (condition.equals("headers")) {
                spec.cookie("x-csrf-token", this.header);
            } else {
                throw new IllegalArgumentException("Condition is known" + condition);
            }

            Response responceForCheck = spec.get().andReturn();
            Assertions.asserJsonByName(responceForCheck, "user_id", 0);
        }
    }

