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

@RunWith(JUnit4.class)
public class JSONObjectTest {
    static JSONObject object;
    static StringBuilder expected;

    private void runTest() {
        expected.append('}');
        object.formatObject();
        System.out.println("Expected : " + expected.toString());
        System.out.println("Actual   : " + object.getNotation());
        assertEquals(expected.toString(), object.getNotation());
    }

    @Before
    public void initObject() {
        object = new JSONObject();
        expected = new StringBuilder().append('{');
    }

    @Test
    public void testAddAndGet() {
        JSONAttribute attribute = new JSONAttribute<>("blaze it", 420);
        object.add(attribute);
        assertEquals(1, object.getValues().size());
        assertEquals(attribute, object.getValues().get(0));
    }

    @Test
    public void testEmpty() {
        runTest();
    }

    @Test
    public void testWithSingleValue() {
        object.add(new JSONAttribute<>("int", 234));
        expected.append('"').append("int").append("\" : ").append(234);

        runTest();
    }

    @Test
    public void testWithMultipleValues() {
        object.add(new JSONAttribute<>("string", "skrrt dab on 'em haters"));
        expected.append('"').append("string").append("\" : ").append("\"skrrt dab on 'em haters\"");

        object.add(new JSONAttribute<>("bool", false));
        expected.append(",\"").append("bool").append("\" : ").append("false");

        object.add(new JSONAttribute<>("int", -234));
        expected.append(',').append('"').append("int").append("\" : ").append(-234);

        runTest();
    }

    @Test
    public void testNestedObject() {
        JSONObject nested = new JSONObject();
        nested.add(new JSONAttribute<>("nested nested", new JSONObject()));
        nested.add(new JSONAttribute<>("key", "value"));
        object.add(new JSONAttribute<>("nested", nested));

        expected.append("\"nested\" : ").append('{').append("\"nested nested\" : {}")
                .append(',').append("\"key\" : \"value\"").append('}');

        runTest();
    }

    @Test
    public void testCollection() {
        JSONObject nested = new JSONObject();
        nested.add(new JSONAttribute<>("nested nested", new JSONObject()));
        nested.add(new JSONAttribute<>("int", 420));

        object.add(new JSONAttribute<>("array",
                Arrays.asList("fsdfafd", 233, true, null, nested)));
        expected.append("\"array\" : [").append("\"fsdfafd\"").append(',').append(233).append(',').append("true")
                .append(',').append("null").append(',').append("{\"nested nested\" : {},")
                .append("\"int\" : ").append(420).append('}').append(']');

        runTest();
    }
}
