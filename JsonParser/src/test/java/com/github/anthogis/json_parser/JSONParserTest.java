package com.github.anthogis.json_parser;

import com.github.anthogis.json_parser.api.JSONAttribute;
import com.github.anthogis.json_parser.api.JSONObject;
import com.github.anthogis.json_parser.api.JSONParser;
import com.github.anthogis.json_parser.api.JSONWriter;
import com.github.anthogis.json_parser.utils.JSONFormatter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class JSONParserTest {

    /**
     * Tests that a file which is parsed produces an identical file when written
     *
     * @throws IOException if there's an error reading/accessing test files.
     */
    @Test
    public void parseWriteEquals() throws IOException {
        String inputFile = "testIn1.json";
        String outputFile = "testOut1.json";

        JSONParser jr = new JSONParser(inputFile);
        JSONObject jo = jr.getParsedObject();

        jo.formatObject();

        List<String> jsonLines = new JSONFormatter(jo).getJsonDataLines();

        new JSONWriter(jo, outputFile, true).writeFile();
        /*
        for (String line : jsonLines) {
            System.out.println(line);
        }*/

        assertTrue(Arrays.equals(Files.readAllBytes(Paths.get(inputFile)),
                Files.readAllBytes(Paths.get(outputFile))));

    }
}
