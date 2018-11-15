package com.github.anthogis.json_parser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import java.util.List;

public class JSONWriter {
    private File jsonFile;
    private Writer writer;
    private List<String> jsonLines;

    public JSONWriter(String jsonData, String fileName) throws IOException {
        JSONFormatter jsonFormatter = new JSONFormatter(jsonData);
        jsonLines = jsonFormatter.getJsonDataLines();

        jsonFile = new File(this.getClass().getClassLoader().getResource(fileName + ".json").getFile());
        jsonFile.createNewFile();
        writer = new BufferedWriter(new FileWriter(jsonFile, false));
    }

    public void writeFile() {

    }
}
