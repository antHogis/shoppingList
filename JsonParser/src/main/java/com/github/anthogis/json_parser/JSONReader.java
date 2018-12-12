package com.github.anthogis.json_parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.github.anthogis.json_parser.JSONToken.*;

public class JSONReader {
    private List<String> jsonLines;
    private JSONObject object;

    public JSONReader(String filePath) throws IOException, JSONParseException {
        this.jsonLines = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line = null;
        //Store string from reader.readline() to line, check if null
        while ((line = reader.readLine()) != null) {
            jsonLines.add(line);
        }
        reader.close();
        object = parseObject();
    }

    private JSONObject parseObject() throws JSONParseException {
        JSONObject object = null;
        List<JSONToken> expectedTokens = new ArrayList();
        expectedTokens.add(OBJECT_BEGIN);

        JSONTokenizer tokenizer = new JSONTokenizer(jsonLines);

        tokenizer.tokenize();

        String key = "";
        List<JSONAttribute> attributeList = new ArrayList<>();
        boolean insideArray = false;

        for (Pair<JSONToken, String> pair : tokenizer.getTokens()) {
            JSONToken token = pair.getKey();
            String value = pair.getValue();

            if (!tokenIsExpected(token, expectedTokens)) {
                throw new JSONParseException();
            }

            boolean addValue = false;
            JSONAttribute attribute = null;

            switch (token) {
                case OBJECT_BEGIN:
                    object = new JSONObject();
                    expectedTokens = expectKeyList();
                    break;
                case KEY:
                    key = value;
                    expectedTokens = expectAssignList();
                    break;
                case ASSIGN:
                    expectedTokens = expectValueList();
                    break;
                case NUMBER:
                    attribute = new JSONAttribute(key, Integer.parseInt(value));
                    addValue = true;
                    break;
                case STRING:
                    attribute = new JSONAttribute(key, value);
                    addValue = true;
                    break;
                case BOOLEAN:
                    attribute = new JSONAttribute(key, Boolean.parseBoolean(value));
                    addValue = true;
                    break;
                case NULL:
                    attribute = new JSONAttribute(key, null);
                    addValue = true;
                    break;
                case DELIMITER:
                    expectedTokens = expectAfterDelimiter(insideArray);
                    break;
                case ARRAY_BEGIN:
                    insideArray = true;
                    expectedTokens = expectValueList();
                    break;
                case ARRAY_END:
                    insideArray = false;
                    addValue = true;

                    attribute = new JSONAttribute(key, attributeList);
                    attributeList = new ArrayList<>();
                    break;
            }

            if (addValue) {
                if (insideArray) {
                    attributeList.add(attribute);
                } else {
                    object.addAttribute(attribute);
                }
                expectedTokens = expectAfterValue(insideArray);
            }
        }

        return object;
    }

    private boolean tokenIsExpected(JSONToken actualToken, List<JSONToken> expectedTokens) {
        boolean syntaxIsValid = false;

        for (JSONToken expectedToken : expectedTokens) {
            if (actualToken == expectedToken) {
                syntaxIsValid = true;
            }
        }

        return syntaxIsValid;
    }

    private List<JSONToken> expectValueList() {
        return Arrays.asList(OBJECT_BEGIN, NULL, BOOLEAN, NUMBER, STRING, ARRAY_BEGIN);
    }

    private List<JSONToken> expectAssignList() {
        return Arrays.asList(ASSIGN);
    }

    private List<JSONToken> expectKeyList() {
        return Arrays.asList(KEY);
    }

    private List<JSONToken> expectAfterValue(boolean insideArray)  {
        return insideArray ? Arrays.asList(DELIMITER, ARRAY_END)
                : Arrays.asList(DELIMITER, OBJECT_END);
    }

    private List<JSONToken> expectAfterDelimiter(boolean insideArray) {
        return insideArray ? expectValueList() : Arrays.asList(KEY);
    }
}