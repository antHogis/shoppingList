package com.github.anthogis.json_parser;

import com.github.anthogis.json_parser.api.JSONAttribute;
import com.github.anthogis.json_parser.api.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.AbstractList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * Tests JSONObject.
 *
 * @author antHogis
 * @version 1.3
 * @since 1.3
 */
public class JSONObjectTest {
    /**
     * The constructed object.
     */
    static JSONObject object;

    /**
     * Contains the expected notation of object.
     */
    static StringBuilder expected;

    /**
     * Helper method for running tests.
     *
     * <ul>
     *     <li>Appends a }-symbol to the end of the expected notation.</li>
     *     <li>Prints the expected and actual notation of object.</li>
     *     <li>Asserts that the actual notation of object is equal to the expected string.</li>
     * </ul>
     */
    private void runTest() {
        expected.append('}');
        String objectNotation = object.getNotation();
        System.out.println("Expected : " + expected.toString());
        System.out.println("Actual   : " + objectNotation);
        assertEquals(expected.toString(), objectNotation);
    }

    /**
     * Initializes test objects, runs before every test method.
     */
    @Before
    public void initObject() {
        object = new JSONObject();
        expected = new StringBuilder().append('{');
    }

    /**
     * Tests the add and getter methods of JSONObject.
     */
    @Test
    public void testAddAndGet() {
        JSONAttribute attribute = new JSONAttribute<>("blaze it", 420);
        object.add(attribute);
        assertEquals(1, object.getValues().size());
        assertEquals(attribute, object.getValues().get(0));
    }

    /**
     * Tests the notation of an empty object.
     */
    @Test
    public void testEmpty() {
        runTest();
    }

    /**
     * Tests the notation of an object with a single value.
     */
    @Test
    public void testWithSingleValue() {
        object.add(new JSONAttribute<>("int", 234));
        expected.append('"').append("int").append("\": ").append(234);

        runTest();
    }

    /**
     * Tests the notation of an object with multiple values.
     */
    @Test
    public void testWithMultipleValues() {
        object.add(new JSONAttribute<>("string", "skrrt dab on 'em haters"));
        expected.append('"').append("string").append("\": ").append("\"skrrt dab on 'em haters\"");

        object.add(new JSONAttribute<>("bool", false));
        expected.append(",\"").append("bool").append("\": ").append("false");

        object.add(new JSONAttribute<>("int", -234));
        expected.append(',').append('"').append("int").append("\": ").append(-234);

        runTest();
    }

    /**
     * Test the notation of an object with nested objects.
     */
    @Test
    public void testNestedObject() {
        JSONObject nested = new JSONObject();
        nested.add(new JSONAttribute<>("nested nested", new JSONObject()));
        nested.add(new JSONAttribute<>("key", "value"));
        object.add(new JSONAttribute<>("nested", nested));

        expected.append("\"nested\": ").append('{').append("\"nested nested\": {}")
                .append(',').append("\"key\": \"value\"").append('}');

        runTest();
    }

    /**
     * Tests the notation of an object with an array.
     */
    @Test
    public void testCollection() {
        JSONObject nested = new JSONObject();
        nested.add(new JSONAttribute<>("nested nested", new JSONObject()));
        nested.add(new JSONAttribute<>("int", 420));

        object.add(new JSONAttribute<>("array",
                Arrays.asList("fsdfafd", 233, true, null, nested)));
        expected.append("\"array\": [").append("\"fsdfafd\"").append(',').append(233).append(',').append("true")
                .append(',').append("null").append(',').append("{\"nested nested\": {},")
                .append("\"int\": ").append(420).append('}').append(']');

        runTest();
    }
}
