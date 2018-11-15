package com.github.anthogis.shoppinglist;

import com.github.anthogis.json_parser.JSONAttribute;

import static org.junit.Assert.*;

import com.github.anthogis.json_parser.JSONObject;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class JSONObjectTest {
    static JSONObject testObjectOne;

    @BeforeClass
    public static void init() {
        testObjectOne = new JSONObject();
        testObjectOne.getAttributes().add(new JSONAttribute<Integer>("fsds", 12));
        testObjectOne.getAttributes().add(new JSONAttribute<Boolean>("very gay", true));
        testObjectOne.formatObject();
    }

    @Test
    public void testNotation() {
        System.out.println(testObjectOne.getNotation());
        assertTrue(testObjectOne.getNotation().equals("{\"fsds\" : 12,\"very gay\" : true}"));
    }
}
