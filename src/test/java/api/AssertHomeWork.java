package api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AssertHomeWork {

    @ParameterizedTest
    @ValueSource(strings= {"qqqqqqqqqqqqqqqqq", "qwwwwwwwwqq"})
    public void test(String strings) {
        assertTrue(strings.length() > 15, "length < 15 symbol");
    }
}

