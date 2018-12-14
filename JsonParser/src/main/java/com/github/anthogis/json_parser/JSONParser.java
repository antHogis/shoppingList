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

        List<Pair<JSONToken, String>> jsonTokens
                = new JSONTokenizer(jsonLines).tokenize().getTokens();
        inspectTokenSyntax(jsonTokens);
        parsedObject = parseObject(jsonTokens);
    }

    public JSONObject getParsedObject() {
        return parsedObject;
    }

    public void inspectTokenSyntax(List<Pair<JSONToken, String>> jsonTokens)
            throws JSONParseException {
        List<JSONToken> expectedTokens = new ArrayList<>();
        expectedTokens.add(OBJECT_BEGIN);

        boolean insideArray = false;

        for (int i = 0; i < jsonTokens.size(); i++) {
            String token = jsonTokens.get(i).getSecond().equals("") ?
                    jsonTokens.get(i).getFirst().name() :
                    jsonTokens.get(i).getFirst().name() + " | " + jsonTokens.get(i).getSecond();
            System.out.println(i + ". " + token);
        }

        mainLoop:
        for (int i = 0; i < jsonTokens.size(); i++) {
            JSONToken token = jsonTokens.get(i).getFirst();

            if (!tokenIsExpected(token, expectedTokens)) {
                StringBuilder messageString = new StringBuilder();

                messageString.append("Unexpected token (number " + i + ")! Expected: ");

                for (JSONToken expectedToken : expectedTokens) {
                    messageString.append(expectedToken.name()).append(", ");
                }

                messageString.delete(messageString.lastIndexOf(", "), messageString.length());
                messageString.append(". Actual: ").append(token.name());

                throw new JSONParseException(messageString.toString());
            }

            switch (token) {
                case OBJECT_BEGIN:
                    expectedTokens = expectKeyList();
                    break;
                case OBJECT_END:
                    expectAfterValue(insideArray);
                    break;
                case KEY:
                    expectedTokens = expectAssignList();
                    break;
                case ASSIGN:
                    expectedTokens = expectValueList();
                    break;
                case DELIMITER:
                    expectedTokens = expectAfterDelimiter(insideArray);
                    break;
                case ARRAY_BEGIN:
                    insideArray = true;
                    expectedTokens = expectValueList();
                    break;
                case ARRAY_END:
                    expectAfterValue((insideArray = false));
                    break;
                case INTEGER:
                case STRING:
                case BOOLEAN:
                case NULL:
                    expectedTokens = expectAfterValue(insideArray);
                    break;
            }

        }
    }

    private JSONObject parseObject(List<Pair<JSONToken, String>> jsonTokens)
            throws JSONParseException {
        JSONObject object = null;
        List<JSONToken> expectedTokens = new ArrayList<>();
        expectedTokens.add(OBJECT_BEGIN);

        String key = "";
        List<JSONAttribute> attributeList = new ArrayList<>();
        boolean insideArray = false;
        boolean isOuterObject = true;

        for (int i = 0; i < jsonTokens.size(); i++) {
            JSONToken token = jsonTokens.get(i).getFirst();
            String value = jsonTokens.get(i).getSecond();

            boolean addValue = false;
            JSONAttribute attribute = null;

            switch (token) {
                case OBJECT_BEGIN:
                    if (isOuterObject) {
                        object = new JSONObject();
                        isOuterObject = false;
                    } else {
                        int closeIndex = getObjectCloseIndex(jsonTokens, i);
                        List<Pair<JSONToken, String>> nestedObjectTokens
                                = jsonTokens.subList(i, closeIndex);
                        object.addAttribute(new JSONAttribute<>(key,
                                parseObject(nestedObjectTokens)));
                        nestedObjectTokens.clear();
                    }
                    break;
                case OBJECT_END:
                    return object;
                case KEY:
                    key = value;
                    break;
                case ASSIGN:
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
                    break;
                case ARRAY_BEGIN:
                    insideArray = true;
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

    private int getObjectCloseIndex(List<Pair<JSONToken, String>> jsonTokens, int start) {
        int closeIndex = -1;
        int nestedObjects = 0;

        for (int i = start + 1; i < jsonTokens.size(); i++) {
            JSONToken token = jsonTokens.get(i).getFirst();
            if (token == OBJECT_BEGIN) {
                nestedObjects++;
            }

            if (token == OBJECT_END && nestedObjects > 0) {
                nestedObjects--;
            } else if (token == OBJECT_END) {
                closeIndex = i;
                break;
            }
        }

        return closeIndex;
    }
}