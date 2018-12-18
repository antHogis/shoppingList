package com.github.anthogis.json_parser;

import com.github.anthogis.json_parser.api.JSONAttribute;
import com.github.anthogis.json_parser.api.JSONObject;
import com.github.anthogis.json_parser.utils.JSONFormatter;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class JSONFormatterTest {
    static JSONObject object;
    static List<String> formattedObject;
    static final String spaces = "    ";

    @BeforeClass
    public static void initObject() {
        object = new JSONObject();
        JSONObject nested = new JSONObject();
        JSONObject nestedNested = new JSONObject();

        nestedNested.add(new JSONAttribute<>("array", Arrays.asList("testString", 34)));
        nested.add(new JSONAttribute<>("nested nested", nestedNested));
        object.add(new JSONAttribute<>("nested", nested));

        formattedObject = new JSONFormatter(object).getJsonDataLines();

        for (String line : formattedObject) {
            System.out.println(line);
        }
    }

    private void runTest(Object expected, Object actual) {
        System.out.println("Expected : " + expected);
        System.out.println("Actual   : " + actual);
        assertEquals(expected, actual);
    }

    @Test
    public void countLines() {
        System.out.println("Test line count");
        runTest(10, formattedObject.size());
    }

    @Test
    public void testIndentation() {
        System.out.print("Test indentation...");
        assertTrue(formattedObject.get(0).startsWith(buildSpaces(0)));
        assertTrue(formattedObject.get(1).startsWith(buildSpaces(1)));
        assertTrue(formattedObject.get(2).startsWith(buildSpaces(2)));
        assertTrue(formattedObject.get(3).startsWith(buildSpaces(3)));
        assertTrue(formattedObject.get(4).startsWith(buildSpaces(4)));
        assertTrue(formattedObject.get(5).startsWith(buildSpaces(4)));
        assertTrue(formattedObject.get(6).startsWith(buildSpaces(3)));
        assertTrue(formattedObject.get(7).startsWith(buildSpaces(2)));
        assertTrue(formattedObject.get(8).startsWith(buildSpaces(1)));
        assertTrue(formattedObject.get(9).startsWith(buildSpaces(0)));
        System.out.println(" successful.");
    }

    private String buildSpaces(int amount) {
        StringBuilder spaceBuilder = new StringBuilder();

        for (int i = 0; i < amount; i++) {
            spaceBuilder.append(spaces);
        }

        return spaceBuilder.toString();
    }
}
