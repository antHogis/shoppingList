package com.github.anthogis.json_parser.api;

import com.github.anthogis.json_parser.utils.JSONContainer;
import com.github.anthogis.json_parser.utils.JSONFormatter;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Writes a JSONObject to a JSON file.
 *
 * <p>Writes a JSONObject to a JSON file, after formatting the JSONObject's notation by using a JSONFormatter, which
 * returns the JSONObject's notation as a list of lines.</p>
 *
 * @author antHogis
 * @version 1.3
 * @since 1.0
 */
public class JSONWriter {
    /**
     * The writer that writes the JSON file.
     */
    private Writer writer;

    /**
     * The list of lines for the JSON file.
     */
    private List<String> jsonLines;

    /**
     * The constructor of JSONWriter.
     *
     * <p>The constructor of JSONWriter initializes jsonLines by processing the JSONObject with a JSONFormatter, which
     * returns a list of lines. The Writer writer is intialized as well, and is given the filename String from arguments,
     * which is used as the filename when writing the file.</p>
     *
     * @param jsonContainer the JSONObject to write to a JSON file.
     * @param fileName the name of the JSON file.
     * @throws IOException if writer's construction fails
     */
    public JSONWriter(JSONContainer jsonContainer, String fileName, boolean addSuffix) throws IOException {
        jsonLines = new JSONFormatter(jsonContainer).getJsonDataLines();

        fileName = addSuffix && !fileName.endsWith(".json") ? fileName + ".json" : fileName;

        writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
                fileName), StandardCharsets.UTF_8));
    }

    /**
     * Writes the JSON file and closes the writer.
     *
     * @return true is the write was successful.
     */
    public boolean writeFile() {
        boolean writeSuccessful = false;

        try {
            for (String line : jsonLines) {
                writer.write(line);
                ((BufferedWriter) writer).newLine();
            }
            writer.flush();
            writeSuccessful = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return writeSuccessful;
    }
}
