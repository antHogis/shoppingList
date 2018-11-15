package com.github.anthogis.json_parser;

import java.util.ArrayList;
import java.util.List;

public class JSONFormatter {
    private String jsonData;
    private List<String> jsonDataLines;

    public JSONFormatter(String jsonData) {
        this.jsonData = jsonData;
        this.jsonDataLines = new ArrayList<>();
    }

    public void formatData() {
        jsonDataLines.add(jsonData);
    }

    public List<String> getJsonDataLines() {
        return jsonDataLines;
    }
}
