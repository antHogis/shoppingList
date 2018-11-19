package com.github.anthogis.shoppinglist;

import com.github.anthogis.json_parser.JSONAttribute;

import static org.junit.Assert.*;

import com.github.anthogis.json_parser.JSONObject;
import com.github.anthogis.json_parser.JSONWriter;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RunWith(JUnit4.class)
public class JSONObjectTest {
    static JSONObject testObjectOne;
    static JSONObject testObjectTwo;

    @BeforeClass
    public static void init() {
        testObjectTwo = new JSONObject();
        testObjectTwo.getAttributes().add(new JSONAttribute<Integer>("fsds", 12));
        testObjectTwo.getAttributes().add(new JSONAttribute<Boolean>("boolean", true));
        ArrayList<String> al = new ArrayList<>();
        al.add("derp1");
        al.add("derp2");
        al.add("derp3");
        ArrayList<Integer> al2 = new ArrayList<>();
        testObjectTwo.getAttributes().add(new JSONAttribute<List>("list", al));
        testObjectTwo.getAttributes().add(new JSONAttribute<List>("list 2", al2));
        testObjectTwo.getAttributes().add(new JSONAttribute<String>("strink", null));

        testObjectOne = new JSONObject();
        testObjectOne.getAttributes().add(new JSONAttribute<Integer>("fsds", 12));
        testObjectOne.getAttributes().add(new JSONAttribute<Boolean>("boolean", true));
        testObjectOne.getAttributes().add(new JSONAttribute<List>("list", al));
        testObjectOne.getAttributes().add(new JSONAttribute<List>("list 2", al2));
        testObjectOne.getAttributes().add(new JSONAttribute<String>("strink", null));
        testObjectOne.getAttributes().add(new JSONAttribute<JSONObject>("object", testObjectTwo));
        testObjectOne.formatObject();
    }

    @Test
    public void testNotation() {
        System.out.println(testObjectOne.getNotation());
        //assertTrue(testObjectOne.getNotation().equals("{\"fsds\" : 12,\"very gay\" : true}"));
    }


    @Test
    public void testWriter() throws IOException {
        JSONWriter jsonWriter = new JSONWriter(testObjectOne, "test");
        assertTrue(jsonWriter.writeFile());
    }
}
