package com.github.anthogis.json_parser;

import com.github.anthogis.json_parser.api.JSONObject;
import com.github.anthogis.json_parser.api.JSONParser;
import com.github.anthogis.json_parser.api.JSONWriter;
import com.github.anthogis.json_parser.utils.JSONParseException;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests class JSONParser.
 *
 * @author antHogis
 * @version 1.3
 * @since 1.3
 */
public class JSONParserTest {

    /**
     * Tests that a file which is parsed produces an identical file when written.
     *
     * Tests that a file which is parsed produces an identical file when written. Input file:
     *
     * {
     *     "testFloat" : 2.2,
     *     "testString" : "23$.*asdv:[]{}",
     *     "testBooleanTrue" : true,
     *     "testBooleanFalse" : false,
     *     "testNull" : null,
     *     "testEmptyArray" : [],
     *     "testArray2" : [
     *         {
     *             "keyWord1" : {},
     *             "keyWord2" : "a string"
     *         },
     *         -3,
     *         true,
     *         4,
     *         []
     *     ],
     *     "testNested" : {
     *         "testNestedInt" : 2,
     *         "testedNestedNested" : {
     *             "testNestedBool" : false,
     *             "testNestedString" : "a string"
     *         }
     *     }
     * }
     *
     * @throws IOException if there's an error reading/accessing test files.
     */
    @Test
    public void parseWriteEquals() throws IOException {
        System.out.println("TEST PARSE WRITE EQUALS");

        String inputFile = "testIn2.json";
        String outputFile = "testOut1.json";

        JSONObject jo = (JSONObject) new JSONParser(inputFile).getParsedContainer();
        new JSONWriter(jo, outputFile, true).writeFile();

        assertTrue(Arrays.equals(Files.readAllBytes(Paths.get(inputFile)),
                Files.readAllBytes(Paths.get(outputFile))));

    }

    /**
     * Helper method for parsing malformed test files that are numbered in the file names.
     * @param first the number of the first file to attempt to parse.
     * @param last the number of the last file to attempt to parse.
     * @throws IOException if the file is not accessible.
     */
    private void parserLooper(int first, int last) throws IOException{
        ArrayList<String> testfiles = new ArrayList<>();

        for (int i = first; i <= last; i++) {
            testfiles.add(String.format("test-malformed-%d.json", i));
        }

        for (String file : testfiles) {
            boolean threwException = false;
            try {
                new JSONParser(file);
            } catch (JSONParseException e) {
                e.printStackTrace();
                threwException = true;
            }

            assertTrue(threwException);
        }
    }

    /**
     * Test that parser throws exception if file starts with something else than whitespace or '{'
     * @throws IOException
     */
    @Test
    public void testInvalidSyntax() throws IOException {
        System.out.println("TEST INVALID SYNTAX");
        parserLooper(1,7);
    }

    /**
     * Tests that values are parsed correctly.
     * @throws IOException if file could not be read.
     */
    @Test
    public void testParsedValuesAndKeys() throws IOException {
        JSONObject jo = (JSONObject) new JSONParser("testIn2.json").getParsedContainer();

        assertEquals(2.2, jo.getValues().get(0).getValue());
        assertEquals(false, jo.getValues().get(3).getValue());
        int testNestedInt = ((int) ((JSONObject) jo.getAttribute("testNested")
                .getValue()).getAttribute("testNestedInt").getValue());
        assertEquals(2, testNestedInt);
    }
}
