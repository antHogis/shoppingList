package com.github.anthogis.json_parser;

import com.github.anthogis.json_parser.api.JSONAttribute;
import com.github.anthogis.json_parser.api.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Tests JSONAttribute.
 *
 * @author antHogis
 * @version 1.3
 * @since 1.3
 */
public class JSONAttributeTest {
    /**
     * The attribute constructed.
     */
    static JSONAttribute attribute;

    /**
     * The expected notation of attribute.
     */
    static String expected;

    /**
     * Tests that notation for an integer attribute is formed properly.
     */
    @Test
    public void testInteger() {
        attribute = new JSONAttribute<>("int", 3);
        expected = "3";

        testHelper();
    }

    /**
     * Tests that notation for a float attribute is formed properly.
     */
    @Test
    public void testFloat() {
        attribute = new JSONAttribute<>("float", 153.239401);
        expected = "153.239401";

        testHelper();
    }

    /**
     * Tests that notation for a string attribute is formed properly.
     */
    @Test
    public void testString() {
        String testString = "test 123123 ^*[]}}{€ YEAS";
        attribute = new JSONAttribute<>("string", testString);
        expected = "\"" + testString + '\"';

        testHelper();
    }

    /**
     * Tests that notation for a boolean attribute is formed properly.
     */
    @Test
    public void testBoolean() {
        attribute = new JSONAttribute<>("bool", true);
        expected = "true";

        testHelper();

        attribute = new JSONAttribute<>("bool", false);
        expected = "false";

        testHelper();
    }

    /**
     * Tests that notation for a null attribute is formed properly.
     */
    @Test
    public void testNull() {
        attribute = new JSONAttribute<>("null", null);
        expected = "null";

        testHelper();
    }

    /**
     * Tests that notation for a JSONObject attribute is formed properly.
     */
    @Test
    public void testJSONObject() {
        JSONObject object = new JSONObject();
        object.add(new JSONAttribute<>("int", 23));
        object.add(new JSONAttribute<>("array", Arrays.asList("fsdfafd", 233, true, null)));
        JSONObject nested = new JSONObject();
        nested.add(new JSONAttribute<>("nested int", 1414));
        object.add(new JSONAttribute<>("nested object", nested));

        attribute = new JSONAttribute<>("object", object);

        StringBuilder expectationBuilder = new StringBuilder();
        expectationBuilder.append("{");
        expectationBuilder.append("\"int\": ").append(23).append(',');
        expectationBuilder.append("\"array\": [").append("\"fsdfafd\"").append(',').append(233)
                .append(',').append("true").append(',').append("null").append("],");
        expectationBuilder.append("\"nested object\": {");
        expectationBuilder.append("\"nested int\": ").append(1414).append('}');
        expectationBuilder.append('}');

        expected = expectationBuilder.toString();

        testHelper();
    }

    /**
     * Tests that notation for a collection attribute is formed properly.
     */
    @Test
    public void testCollection() {
        attribute = new JSONAttribute<>("array", Arrays.asList("fsdfafd", 233, true, null));

        StringBuilder expectationBuilder = new StringBuilder()
                .append("[").append("\"fsdfafd\"").append(',').append(233)
                .append(',').append("true").append(',').append("null").append(']');
        expected = expectationBuilder.toString();

        testHelper();
    }

    /**
     * Tests that notation for a JSONAttribute attribute is formed properly.
     */
    @Test
    public void testJSONAttribute() {
        attribute = new JSONAttribute<>("attribute value",
                new JSONAttribute<>("irrelevant", 2345));
        expected = "2345";

        testHelper();
    }

    /**
     * Tests the getters of JSONAttribute.
     */
    @Test
    public void testGetters() {
        String key = "key";
        int value = 53632133;
        attribute = new JSONAttribute<>(key, value);

        assertEquals(key, attribute.getKeyWord());
        assertEquals(value, attribute.getValue());
    }

    /**
     * A helper method for running tests.
     *
     * Prints the values of expected and attributes notation, and asserts that they are equal.
     */
    private void testHelper() {
        System.out.printf("Expected : %s\n", expected);
        System.out.printf("Actual   : %s\n", attribute.getNotation());

        assertEquals(expected, attribute.getNotation());
    }
}
