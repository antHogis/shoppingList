package com.github.anthogis.json_parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.github.anthogis.json_parser.JSONToken.*;

/**
 * TODO Write javadoc
 *
 * @author antHogis
 * @version 1.3
 * @since 1.2
 */
public class JSONParser {
    private JSONObject parsedObject;

    public JSONParser(String filePath) throws IOException, JSONParseException {
        List<String> jsonLines = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line = null;
        while ((line = reader.readLine()) != null) {
            jsonLines.add(line);
        }
        reader.close();
        parsedObject = parseObject(jsonLines);
    }

    public JSONObject getParsedObject() {
        return parsedObject;
    }

    private JSONObject parseObject(List<String> jsonLines) throws JSONParseException {
        JSONObject object = null;
        List<JSONToken> expectedTokens = new ArrayList<>();
        expectedTokens.add(OBJECT_BEGIN);

        JSONTokenizer tokenizer = new JSONTokenizer(jsonLines);

        tokenizer.tokenize();

        String key = "";
        List<JSONAttribute> attributeList = new ArrayList<>();
        boolean insideArray = false;

        for (Pair<JSONToken, String> pair : tokenizer.getTokens()) {
            System.out.println(pair.getSecond().equals("") ?
                    pair.getFirst().name() :
                    pair.getFirst().name() + " | " + pair.getSecond());
        }

        for (Pair<JSONToken, String> pair : tokenizer.getTokens()) {
            JSONToken token = pair.getFirst();
            String value = pair.getSecond();

            if (!tokenIsExpected(token, expectedTokens)) {
                StringBuilder messageString = new StringBuilder();

                messageString.append("Unexpected token! Expected: ");

                for (JSONToken expectedToken : expectedTokens) {
                    messageString.append(expectedToken.name()).append(", ");
                }

                messageString.delete(messageString.lastIndexOf(", "), messageString.length());
                messageString.append(". Actual: ").append(token.name());

                throw new JSONParseException(messageString.toString());
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
                case INTEGER:
                    attribute = new JSONAttribute<>(key, Integer.parseInt(value));
                    addValue = true;
                    break;
                case STRING:
                    attribute = new JSONAttribute<>(key, value);
                    addValue = true;
                    break;
                case BOOLEAN:
                    attribute = new JSONAttribute<>(key, Boolean.parseBoolean(value));
                    addValue = true;
                    break;
                case NULL:
                    attribute = new JSONAttribute<>(key, null);
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

                    attribute = new JSONAttribute<>(key, attributeList);
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
        boolean actualIsExpected = false;

        for (JSONToken expectedToken : expectedTokens) {
            if (actualToken == expectedToken) {
                actualIsExpected = true;
            }
        }

        return actualIsExpected;
    }

    private List<JSONToken> expectValueList() {
        return Arrays.asList(OBJECT_BEGIN, NULL, BOOLEAN, INTEGER, STRING, ARRAY_BEGIN);
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