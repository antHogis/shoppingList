package com.github.anthogis.json_parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class JSONReader {
    private List<String> jsonLines;
    private JSONObject object;

    public JSONReader(String filePath) throws IOException {
        this.jsonLines = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line = null;
        //Store string from reader.readline() to line, check if null
        while ((line = reader.readLine()) != null) {
            jsonLines.add(line);
        }
        reader.close();
    }
}
