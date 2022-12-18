package lib;

import io.restassured.http.Headers;
import io.restassured.response.Response;

import java.util.Map;

import static org.hamcrest.Matchers.hasKey;
import  static org.junit.jupiter.api.Assertions.assertTrue;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class BaseTestKeys {
    protected  String getHeader(Response Response, String name){
        Headers headers = Response.getHeaders();

        assertTrue(headers.hasHeaderWithName(name), "Response doesnt have header with name " + name);
        return headers.getValue(name);
    }
    protected String getCookie (Response Response, String name){
        Map<String, String> cookies = Response.getCookies();

        assertTrue(cookies.containsKey(name), "Response doesnt have cookie with " + cookies);
        return  cookies.get(name);
    }

    protected int getIntFromJson(Response Response, String name){
        Response.then().assertThat().body("$",hasKey(name));
        return Response.jsonPath().getInt(name);
    }
}
