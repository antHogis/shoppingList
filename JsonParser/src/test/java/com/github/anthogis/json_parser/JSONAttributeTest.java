package com.github.anthogis.json_parser;

import com.github.anthogis.json_parser.api.JSONAttribute;
import com.github.anthogis.json_parser.api.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class JSONAttributeTest {
    static JSONAttribute attribute;
    static String expected;

    @Test
    public void testInteger() {
        attribute = new JSONAttribute<>("int", 3);
        expected = "\"int\" : 3";

        testHelper();
    }

    @Test
    public void testFloat() {
        attribute = new JSONAttribute<>("float", 153.239401);
        expected = "\"float\" : 153.239401";

        testHelper();
    }

    @Test
    public void testString() {
        String testString = "test 123123 ^*[]}}{€ YEAS";
        attribute = new JSONAttribute<>("string", testString);
        expected = "\"string\" : \"" + testString + '\"';

        testHelper();
    }

    @Test
    public void testBoolean() {
        attribute = new JSONAttribute<>("bool", true);
        expected = "\"bool\" : true";

        testHelper();

        attribute = new JSONAttribute<>("bool", false);
        expected = "\"bool\" : false";

        testHelper();
    }

    @Test
    public void testNull() {
        attribute = new JSONAttribute<>("null", null);
        expected = "\"null\" : null";

        testHelper();
    }

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
        expectationBuilder.append("\"object\" : {");
        expectationBuilder.append("\"int\" : ").append(23).append(',');
        expectationBuilder.append("\"array\" : [").append("\"fsdfafd\"").append(',').append(233)
                .append(',').append("true").append(',').append("null").append("],");
        expectationBuilder.append("\"nested object\" : {");
        expectationBuilder.append("\"nested int\" : ").append(1414).append('}');
        expectationBuilder.append('}');

        expected = expectationBuilder.toString();

        testHelper();
    }

    @Test
    public void testCollection() {
        attribute = new JSONAttribute<>("array", Arrays.asList("fsdfafd", 233, true, null));

        StringBuilder expectationBuilder = new StringBuilder()
                .append("\"array\" : [").append("\"fsdfafd\"").append(',').append(233)
                .append(',').append("true").append(',').append("null").append(']');
        expected = expectationBuilder.toString();

        testHelper();
    }

    @Test
    public void testJSONAttribute() {
        attribute = new JSONAttribute<>("attribute value",
                new JSONAttribute<>("irrelevant", 2345));
        expected = "\"attribute value\" : " + 2345;

        testHelper();
    }

    @Test
    public void testGetters() {
        String key = "key";
        int value = 53632133;
        attribute = new JSONAttribute<>(key, value);

        assertEquals(key, attribute.getKeyWord());
        assertEquals(value, attribute.getValue());
    }

    private void testHelper() {
        System.out.printf("Expected : %s\n", expected);
        System.out.printf("Actual   : %s\n", attribute.getNotation());

        assertEquals(expected, attribute.getNotation());
    }
}
