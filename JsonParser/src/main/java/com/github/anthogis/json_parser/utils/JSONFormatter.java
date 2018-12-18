package com.github.anthogis.json_parser.utils;

import com.github.anthogis.json_parser.api.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * JSONFormatter formats single-line JSON data into proper form.
 *
 * <p>JSONFormatter takes the json data from a JSONObject, and separates it into a List of lines with proper indentation.</p>
 *
 * @author antHogis
 * @version 1.3
 * @since 1.0
 */
public class JSONFormatter {
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
        boolean insideString = false;

        for (int i = 0; i < jsonData.length(); i++) {
            char currentChar = jsonData.charAt(i);
            boolean createNewLine = false;

            if (unescapedQuoteFound(i)) {
                insideString = !insideString;
            }

            line.append(currentChar);

            if (!insideString) {
                if (i + 2 < jsonData.length() && jsonData.substring(i, i + 2).matches("\\{}|\\[]")) {
                    line.append(jsonData.charAt(i + 1));
                    i++;
                } else
                if (currentChar == '{' || currentChar == '[') {
                    indents++;
                    createNewLine = true;
                } else if (currentChar == ',') {
                    createNewLine = true;
                }

                if (i < jsonData.length() - 1) {
                    if (jsonData.charAt(i + 1) == '}' || jsonData.charAt(i + 1) == ']') {
                        indents--;
                        createNewLine = true;
                    }
                } else {
                    createNewLine = true;
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
    }

    /**
     * Returns the formatted jsonData.
     * @return the formatted jsonData.
     */
    public List<String> getJsonDataLines() {
        return jsonDataLines;
    }

    /**
     * Check if the character at the given index in jsonData is an unescaped double quote.
     * @param index the index of the character to inspect.
     * @return true if the character is an unescaped double quote.
     */
    private boolean unescapedQuoteFound(int index) {
        boolean found = false;

        if (jsonData.charAt(index) == '"') {
            found = true;

            for (index = index - 1; index >= 0; index--) {
                if (jsonData.charAt(index) == '\\') {
                    found = !found;
                } else {
                    break;
                }
            }
        }

        return found;
    }
}
