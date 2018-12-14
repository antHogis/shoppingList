package com.github.anthogis.json_parser;

import java.util.ArrayList;
import java.util.List;

/**
 * JSONFormatter formats single-line JSON data into proper form.
 *
 * <p>JSONFormatter takes the json data from a JSONObject, and separates it into a List of lines with proper indentation.</p>
 *
 * @author antHogis
 * @version 1.0
 * @since 1.0
 */
class JSONFormatter {
    /**
     * The JSON data given to JSONFormatter.
     */
    private final String jsonData;

    /**
     * The formatted JSON data.
     */
    private List<String> jsonDataLines;

    /**
     * The constructor for JSONFormatter, also formats the notation of the given jsonObject by calling method formatData.
     * @param jsonObject the jsonObject.
     */
    public JSONFormatter(JSONObject jsonObject) {
        this.jsonData = jsonObject.getNotation();
        formatData();
    }

    /**
     * Formats jsonData, so that it has proper line breaks and indentation.
     */
    private void formatData() {
        this.jsonDataLines = new ArrayList<>();
        final String indent = "    ";
        StringBuilder line = new StringBuilder();
        int indents = 0;

        for (int i = 0; i < jsonData.length(); i++) {
            char currentChar = jsonData.charAt(i);
            boolean createNewLine = false;

            line.append(currentChar);

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
                jsonDataLines.add(line.toString());
                line = new StringBuilder();
                for (int j = 0; j < indents; j++) {
                    line.append(indent);
                }
            }
        }
    }

    /**
     * Returns the formatted jsonData.
     * @return the formatted jsonData.
     */
    public List<String> getJsonDataLines() {
        return jsonDataLines;
    }
}
