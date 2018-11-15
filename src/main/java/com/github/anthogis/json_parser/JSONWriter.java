package com.github.anthogis.json_parser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class JSONWriter {
    private File jsonFile;
    private Writer writer;
    private List<String> jsonLines;

    public JSONWriter(String jsonData, String fileName) throws IOException {
        JSONFormatter jsonFormatter = new JSONFormatter(jsonData);
        jsonLines = jsonFormatter.getJsonDataLines();

        writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
                fileName + ".json"), StandardCharsets.UTF_8));
    }

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
        }

        return writeSuccessful;
    }
}
