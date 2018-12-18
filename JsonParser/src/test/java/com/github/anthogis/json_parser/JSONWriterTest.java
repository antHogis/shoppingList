package com.github.anthogis.json_parser;

import com.github.anthogis.json_parser.api.JSONAttribute;
import com.github.anthogis.json_parser.api.JSONObject;
import com.github.anthogis.json_parser.api.JSONWriter;
import com.github.anthogis.json_parser.utils.JSONFormatter;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests JSONWriter
 *
 * @author antHogis
 * @version 1.3
 * @since 1.3
 */
@RunWith(JUnit4.class)
public class JSONWriterTest {
    static JSONObject object;
    static String fileName;

    /**
     * Initializes values for testing.
     */
    @BeforeClass
    public static void initTest() throws IOException {
        fileName = "test_write.json";

        object = new JSONObject();
        JSONObject nested = new JSONObject();
        JSONObject nestedNested = new JSONObject();

        nestedNested.add(new JSONAttribute<>("array", Arrays.asList("testString", 34)));
        nested.add(new JSONAttribute<>("nested nested", nestedNested));
        object.add(new JSONAttribute<>("nested", nested));

        new JSONWriter(object, fileName, false).writeFile();
    }

    /**
     * Tests that the file exists after writing.
     */
    @Test
    public void testWriteFile() {
        assertTrue(new File(fileName).exists());
    }


    /**
     * Tests that the file is intact after writing.
     * @throws IOException if the written file could not be read.
     * @throws IndexOutOfBoundsException if the file doesn't have the expected amount of lines.
     */
    @Test
    public void testFileIntact() throws IOException, IndexOutOfBoundsException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        List<String> writtenFileLines = reader.lines().collect(Collectors.toList());
        reader.close();

        List<String> objectLines = new JSONFormatter(object).getJsonDataLines();

        for (int i = 0; i < objectLines.size(); i++) {
            assertEquals(objectLines.get(i), writtenFileLines.get(i));
        }
    }

    /**
     * Deletes the file. An exception is thrown if the file is inaccessible, which indicates that a stream is not closed.
     * @throws IOException if the file is inaccessible.
     */
    @AfterClass
    public static void deleteFile() throws IOException{
        if (new File(fileName).exists()) {
            Files.delete(Paths.get(fileName));
        }
    }
}
