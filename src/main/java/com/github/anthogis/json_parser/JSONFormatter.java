package com.github.anthogis.json_parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JSONFormatter {
    private String jsonData;
    private List<String> jsonDataLines;

    public JSONFormatter(String jsonData) {
        this.jsonData = jsonData;
        formatData();
    }

    public void formatData() {
        this.jsonDataLines = new ArrayList<>();
        String indent = "    ";
        String line = "";
        int indents = 0;

        for (int i = 0; i < jsonData.length(); i++) {
            char currentChar = jsonData.charAt(i);
            boolean createNewLine = false;

            line += currentChar;

            if (currentChar == '{' || currentChar == '[') {
                indents++;
                createNewLine = true;
            } else if (currentChar == '}') {
                createNewLine = true;
            } else if (currentChar == ',') {
                createNewLine = true;
            }

            if (i < jsonData.length() - 1) {
                if (jsonData.charAt(i + 1) == '}' || jsonData.charAt(i + 1) == ']') {
                    indents--;
                    createNewLine = true;
                }
            }

            if (createNewLine) {
                jsonDataLines.add(line);
                line = "";
                for (int j = 0; j < indents; j++) {
                    line += indent;
                }
            }
        }
    }

    public List<String> getJsonDataLines() {
        return jsonDataLines;
    }
}
