package com.github.anthogis.json_parser;

import com.github.anthogis.json_parser.api.JSONAttribute;
import com.github.anthogis.json_parser.api.JSONObject;
import com.github.anthogis.json_parser.api.JSONParser;
import com.github.anthogis.json_parser.api.JSONWriter;
import com.github.anthogis.json_parser.utils.JSONFormatter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.List;

@RunWith(JUnit4.class)
public class JSONParserTest {
    @Test
    public void test() throws Exception {
        JSONParser jr = new JSONParser("test.json");
        JSONObject jo = jr.getParsedObject();

        jo.formatObject();

        List<String> jsonLines = new JSONFormatter(jo).getJsonDataLines();

        new JSONWriter(jo, "writeTest", true).writeFile();

        for (String line : jsonLines) {
            System.out.println(line);
        }

        jo = new JSONObject();
        ArrayList<Object> arr = new ArrayList();
        arr.add(new JSONObject());
        arr.add(2);
        jo.add(new JSONAttribute<>("arr", arr));

        jo.formatObject();

        jsonLines = new JSONFormatter(jo).getJsonDataLines();

        for (String line : jsonLines) {
            System.out.println(line);
        }

    }
}
