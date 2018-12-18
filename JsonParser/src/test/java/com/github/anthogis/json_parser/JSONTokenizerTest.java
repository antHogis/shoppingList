package com.github.anthogis.json_parser;

import com.github.anthogis.json_parser.api.JSONAttribute;
import com.github.anthogis.json_parser.api.JSONObject;
import com.github.anthogis.json_parser.utils.JSONFormatter;
import com.github.anthogis.json_parser.utils.JSONToken;
import com.github.anthogis.json_parser.utils.JSONTokenizer;
import com.github.anthogis.json_parser.utils.Pair;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static com.github.anthogis.json_parser.utils.JSONToken.*;

/**
 * Tests JSONTokenizer
 *
 * @author antHogis
 * @version 1.3
 * @since 1.3
 */
@RunWith(JUnit4.class)
public class JSONTokenizerTest {
    static JSONObject object;
    static List<Pair<JSONToken, String>> actualTokens;
    static List<JSONToken> expectedTokens;

    /**
     * Initializes test objects.
     */
    @Before
    public void initObjects() {
        expectedTokens = new ArrayList<>();
        object = new JSONObject();

        expectedTokens.add(OBJECT_BEGIN);
    }

    /**
     * Helper method for running tests, compares actualTokens and expectedTokens.
     *
     * <ul>
     *     <li>Adds OBJECT_END Token to expectedTokens.</li>
     *     <li>Asserts that actualTokens and expectedTokens are equal in size.</li>
     *     <li>Prints values of tokens.</li>
     *     <li>Asserts that they contain the same tokens in the same order.</li>
     * </ul>
     */
    private void runTest() {
        expectedTokens.add(OBJECT_END);

        actualTokens = new JSONTokenizer(new JSONFormatter(object).getJsonDataLines())
                .tokenize().getTokens();

        System.out.println("ACTUAL TOKENS:");
        for (Pair<JSONToken, String> pair : actualTokens) {
            System.out.println(pair.getFirst().name());
        }

        assertEquals(expectedTokens.size(), actualTokens.size());

        for (int i = 0; i < expectedTokens.size(); i++) {
            JSONToken actualToken = actualTokens.get(i).getFirst();
            JSONToken expectedToken = expectedTokens.get(i);

            System.out.println("Expected : " + expectedToken.name());
            System.out.println("Actual   : " + actualToken.name());

            assertEquals(expectedToken, actualToken);
        }
    }

    /**
     * Adds a value to a new JSONAttribute of object.
     * @param value the value to add
     * @param <T> the type of value
     */
    private <T> void addAttribute(T value) {
        object.add(new JSONAttribute<>("test", value));
    }

    /**
     * Adds a token to expectedTokens, the token depends on the type of value.
     * @param value the value to add.
     * @param <T> the type of value.
     */
    private <T> void addExpectedTokenByValue(T value) {
        if (value instanceof Integer) {
            expectedTokens.add(INTEGER);
        } else if (value instanceof Double) {
            expectedTokens.add(FLOAT);
        } else if (value instanceof String) {
            expectedTokens.add(STRING);
        } else if (value instanceof Boolean) {
            expectedTokens.add(BOOLEAN);
        } else if (value == null) {
            expectedTokens.add(NULL);
        }
    }

    /**
     * Test tokens of an empty object.
     */
    @Test
    public void testEmpty() {
        System.out.println("--------TEST EMPTY-------");
        runTest();
    }

    /**
     * Test tokens of an object that contains variables of types except objects and arrays.
     */
    @Test
    public void testWithValues() {
        System.out.println("----TEST WITH VALUES----");
        ArrayList<Object> values = new ArrayList<>();
        values.add(23000);
        values.add(-23);
        values.add(1234.56789);
        values.add("asdfsdf234öåö]}{]}");
        values.add(false);
        values.add(true);
        values.add(null);

        for (Object value : values) {
            addAttribute(value);

            expectedTokens.add(KEY);
            expectedTokens.add(ASSIGN);
            addExpectedTokenByValue(value);
            expectedTokens.add(DELIMITER);
        }

        if (expectedTokens.get(expectedTokens.size() - 1) == DELIMITER) {
            expectedTokens.remove(expectedTokens.size() - 1);
        }

        runTest();
    }

    /**
     * Tests tokens of an object that contains arrays.
     */
    @Test
    public void testArrays() {
        System.out.println("------TEST ARRAYS------");
        ArrayList<Object> list = new ArrayList<>();
        list.add(new ArrayList<>());
        list.add("drfdsaf");
        addAttribute(list);
        addAttribute(new ArrayList<>());

        expectedTokens.add(KEY);
        expectedTokens.add(ASSIGN);
        expectedTokens.add(ARRAY_BEGIN);
        expectedTokens.add(ARRAY_BEGIN);
        expectedTokens.add(ARRAY_END);
        expectedTokens.add(DELIMITER);
        expectedTokens.add(STRING);
        expectedTokens.add(ARRAY_END);
        expectedTokens.add(DELIMITER);
        expectedTokens.add(KEY);
        expectedTokens.add(ASSIGN);
        expectedTokens.add(ARRAY_BEGIN);
        expectedTokens.add(ARRAY_END);

        runTest();
    }

    /**
     * Test tokens of object with nested objects.
     */
    @Test
    public void testNested() {
        System.out.println("-----TEST NESTED-----");
        JSONObject nested = new JSONObject();
        nested.add(new JSONAttribute<>("test", new JSONObject()));
        addAttribute(nested);
        addAttribute(Arrays.asList(new JSONObject()));

        //Add nested object to outer object
        expectedTokens.add(KEY);
        expectedTokens.add(ASSIGN);
        expectedTokens.add(OBJECT_BEGIN);
        //Add object to nested object
        expectedTokens.add(KEY);
        expectedTokens.add(ASSIGN);
        expectedTokens.add(OBJECT_BEGIN);
        expectedTokens.add(OBJECT_END);
        expectedTokens.add(OBJECT_END);
        expectedTokens.add(DELIMITER);
        //Add array to outer object
        expectedTokens.add(KEY);
        expectedTokens.add(ASSIGN);
        expectedTokens.add(ARRAY_BEGIN);
        //Add object to array
        expectedTokens.add(OBJECT_BEGIN);
        expectedTokens.add(OBJECT_END);
        expectedTokens.add(ARRAY_END);

        runTest();
    }

}
