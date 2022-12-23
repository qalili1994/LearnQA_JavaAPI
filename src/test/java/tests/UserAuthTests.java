package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import lib.BaseTestKeys;
import lib.Assertions;
import lib.ApiCoreRequests;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import java.util.HashMap;
import java.util.Map;

@Epic("Authorization cases")
@Feature("Authorization")

    public class UserAuthTests extends BaseTestKeys {

        String cookie;
        String header;
        int userIdOnAuth;

        private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

        @BeforeEach
        public void loginUser() {
            Map<String, String> authData = new HashMap<>();
            authData.put("email", "vinkotov@example.com");
            authData.put("password", "1234");

            Response responseGetAuth = apiCoreRequests
                    .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

            this.cookie = this.getCookie(responseGetAuth, "auth_sid");
            this.header = this.getHeader(responseGetAuth,"x-csrf-token");
            this.userIdOnAuth = this.getIntFromJson(responseGetAuth,"user_id");
        }

        @Test
        @Description("This test successfully authorize user by email and password")
        @DisplayName("Test positive auth user")
        public void testAuthUser() {
            Response responseCheckAuth = apiCoreRequests
                    .makeGetRequest(
                            "https://playground.learnqa.ru/api/user/auth",
                            this.header,
                            this.cookie
                    );

            Assertions.asserJsonByName(responseCheckAuth, "user_id", this.userIdOnAuth);
        }

        @Description("This test checks authorization status w/o sending auth cookie or token")
        @DisplayName("Test negative")
        @ParameterizedTest
        @ValueSource(strings = {"cookie", "headers"})
        public void testNegativeAuthUser(String condition) {

            if (condition.equals("cookie")) {
                Response responseForCheck = apiCoreRequests.makeGetRequestWithCookie(
                        "https://playground.learnqa.ru/api/user/auth",
                        this.cookie
                );
                Assertions.asserJsonByName(responseForCheck, "user_id", 0);
            } else if (condition.equals("headers")) {
                Response responseForCheck = apiCoreRequests.makeGetRequestWithToken(
                        "https://playground.learnqa.ru/api/user/auth",
                        this.header
                );
                Assertions.asserJsonByName(responseForCheck, "user_id", 0);
            } else {
                throw new IllegalArgumentException("Condition is known" + condition);
            }
        }
    }

