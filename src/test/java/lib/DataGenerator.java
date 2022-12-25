package lib;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.params.provider.Arguments;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.stream.Stream;

public class DataGenerator {
    public static String longName = RandomStringUtils.randomAlphabetic(251);
    public static String shortName = RandomStringUtils.randomAlphabetic(1);

    public static String getRandomEmail() {
        String timestamp = new SimpleDateFormat("yyyMMddHHmmss").format(new java.util.Date());
        return "learnqa" + timestamp + "@example.ru";
    }

    public static Map<String, String> getRegistrationData() {
        Map<String, String> data = new HashMap<>();
        data.put("email", DataGenerator.getRandomEmail());
        data.put("password", "123");
        data.put("lastName", "learnqa");
        data.put("username", "learqa");
        data.put("firstName", "learnqa");

        return data;
    }

    public static Map<String, String> getRegistrationData(Map<String, String> nonDefaultValues) {
        Map<String, String> defaultValues = DataGenerator.getRegistrationData();

        Map<String, String> userData = new HashMap<>();
        String[] keys = {"email", "password", "username", "lastName", "firstName"};
        for (String key : keys) {
            if (nonDefaultValues.containsKey(key)) {
                userData.put(key, nonDefaultValues.get(key));
            } else {
                userData.put(key, defaultValues.get(key));
            }
        }
        return userData;
    }

    public static Map<String, String> getRegistrationDataDelete(Map<String, String> nonDefaultValues) {
        Map<String, String> defaultValues = DataGenerator.getRegistrationData();

        Map<String, String> userData = new HashMap<>();
        String[] keys = {"email", "password", "username", "lastName", "firstName"};
        for (String key : keys) {
            if (nonDefaultValues.containsKey(key)) {
                userData.remove(key, nonDefaultValues.get(key));
            } else {
                userData.put(key, defaultValues.get(key));
            }
        }
        return userData;
    }

}
