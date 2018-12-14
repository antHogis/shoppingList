package com.github.anthogis.json_parser;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class JSONParserTest {
    @Test
    public void test() throws Exception {
        JSONParser jr = new JSONParser("test.json");
        JSONObject jo = jr.getParsedObject();

        jo.formatObject();

        for (JSONAttribute attribute : jo.getAttributes()) {
            System.out.println(attribute);
        }

        System.out.println(jo.getNotation());
    }
}
