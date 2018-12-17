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
    /**
     * TODO doc field
     */
    private JSONObject parsedObject;

    /**
     * TODO doc constr
     *
     * @param filePath
     * @throws IOException
     * @throws JSONParseException
     */
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
        parsedObject = (JSONObject) parseObject(jsonTokens, JSONObject.class);
    }

    /**
     * TODO doc method
     * @return
     */
    public JSONObject getParsedObject() {
        return parsedObject;
    }

    /**
     * TODO doc method
     *
     * @param jsonTokens
     * @throws JSONParseException
     */
    public void inspectTokenSyntax(List<Pair<JSONToken, String>> jsonTokens)
            throws JSONParseException {
        List<JSONToken> expectedTokens = new ArrayList<>();
        //For a structure of which is the active encapsulating token (ARRAY_BEGIN/OBJECT_BEGIN)
        List<JSONToken> encapsulatingTokens = new ArrayList<>(jsonTokens.size());
        expectedTokens.add(OBJECT_BEGIN);

        for (int i = 0; i < jsonTokens.size(); i++) {
            String token = jsonTokens.get(i).getSecond().equals("") ?
                    jsonTokens.get(i).getFirst().name() :
                    jsonTokens.get(i).getFirst().name() + " | " + jsonTokens.get(i).getSecond();
            System.out.println(i + ". " + token);
        }

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

            boolean insideArray = isInsideArray(token, encapsulatingTokens);


            //PRINT FOR DEBUG
            String tokenString = jsonTokens.get(i).getSecond().equals("") ?
                    token.name() : token.name() + " | " + jsonTokens.get(i).getSecond();
            System.out.printf("%d. %s: %b\n", i, tokenString, insideArray);

            switch (token) {
                case OBJECT_BEGIN:
                    expectedTokens = expectKeyList();
                    break;
                case OBJECT_END:
                    expectedTokens = expectAfterValue(insideArray);
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
                    expectedTokens = expectValueList();
                    break;
                case ARRAY_END:
                    expectedTokens = expectAfterValue(insideArray);
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

    /**
     * TODO doc method
     *
     * @param jsonTokens
     * @return
     * @throws JSONParseException
     */
    private JSONContainer parseObject(List<Pair<JSONToken, String>> jsonTokens, Class<? extends JSONContainer> type)
            throws JSONParseException {
        JSONContainer container = null;
        List<JSONToken> expectedTokens = new ArrayList<>();
        List<JSONToken> encapsulatingTokens = new ArrayList<>();
        expectedTokens.add(OBJECT_BEGIN);

        String key = "";
        List<JSONAttribute> attributeList = new ArrayList<>();
        boolean isOuterObject = false;
        boolean isOuterArray = false;

        if (type == JSONObject.class) {
            isOuterObject = true;
        } else if (type == JSONArray.class) {
            isOuterArray = true;
        }

        for (int i = 0; i < jsonTokens.size(); i++) {
            JSONToken token = jsonTokens.get(i).getFirst();
            String value = jsonTokens.get(i).getSecond();

            boolean addValue = false;
            JSONAttribute attribute = null;

            switch (token) {
                case OBJECT_BEGIN:
                    if (isOuterObject) {
                        container = new JSONObject();
                        isOuterObject = false;
                    } else {
                        int closeIndex = getContainerCloseIndex(jsonTokens, i, JSONObject.class);
                        List<Pair<JSONToken, String>> nestedObjectTokens
                                = jsonTokens.subList(i, closeIndex);
                        attribute = new JSONAttribute<>(key, parseObject(nestedObjectTokens, JSONObject.class));
                        addValue = true;
                        nestedObjectTokens.clear();
                    }
                    break;
                case OBJECT_END:
                    return container;
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
                    if (isOuterArray) {
                        container = new JSONArray();
                        isOuterArray = false;
                    } else {
                        int closeIndex = getContainerCloseIndex(jsonTokens, i, JSONArray.class);
                        List<Pair<JSONToken, String>> nestedTokens
                                = jsonTokens.subList(i, closeIndex);
                        attribute = new JSONAttribute<>(key, parseObject(nestedTokens,
                                JSONArray.class).getAttributes());
                        addValue = true;
                        nestedTokens.clear();
                    }
                    break;
                case ARRAY_END:
                    return container;
            }

            if (addValue) {
                container.add(attribute);
            }
        }

        return container;
    }

    /**
     * TODO doc method
     *
     * @param actualToken
     * @param expectedTokens
     * @return
     */
    private boolean tokenIsExpected(JSONToken actualToken, List<JSONToken> expectedTokens) {
        boolean actualIsExpected = false;

        for (JSONToken expectedToken : expectedTokens) {
            if (actualToken == expectedToken) {
                actualIsExpected = true;
            }
        }

        return actualIsExpected;
    }

    /**
     * TODO doc method
     *
     * @return
     */
    private List<JSONToken> expectValueList() {
        return Arrays.asList(OBJECT_BEGIN, NULL, BOOLEAN, INTEGER, STRING, ARRAY_BEGIN);
    }

    /**
     * TODO doc method
     *
     * @return
     */
    private List<JSONToken> expectAssignList() {
        return Arrays.asList(ASSIGN);
    }

    /**
     * TODO doc method
     *
     * @return
     */
    private List<JSONToken> expectKeyList() {
        return Arrays.asList(KEY, OBJECT_END);
    }

    /**
     * TODO doc method
     *
     * @param insideArray
     * @return
     */
    private List<JSONToken> expectAfterValue(boolean insideArray)  {
        return insideArray ? Arrays.asList(DELIMITER, ARRAY_END)
                : Arrays.asList(DELIMITER, OBJECT_END);
    }

    /**
     * TODO doc method
     *
     * @param insideArray
     * @return
     */
    private List<JSONToken> expectAfterDelimiter(boolean insideArray) {
        return insideArray ? expectValueList() : Arrays.asList(KEY);
    }

    /**
     * TODO doc method
     *
     * @param jsonTokens
     * @param start
     * @return
     */
    private int getContainerCloseIndex(List<Pair<JSONToken, String>> jsonTokens, int start,
                                       Class<? extends JSONContainer> type) {
        int closeIndex = -1;
        int nestedObjects = 0;
        JSONToken beginToken = null;
        JSONToken endToken = null;

        if (type == JSONObject.class) {
            beginToken = OBJECT_BEGIN;
            endToken = OBJECT_END;
        } else if (type == JSONArray.class) {
            beginToken = ARRAY_BEGIN;
            endToken = ARRAY_END;
        }

        for (int i = start + 1; i < jsonTokens.size(); i++) {
            JSONToken token = jsonTokens.get(i).getFirst();
            if (token == beginToken) {
                nestedObjects++;
            }

            if (token == endToken && nestedObjects > 0) {
                nestedObjects--;
            } else if (token == endToken) {
                closeIndex = i;
                break;
            }
        }

        return closeIndex;
    }


    /**
     * TODO doc method
     *
     * @param latestToken
     * @param encapsulatingTokens
     * @return
     */
    private boolean isInsideArray(JSONToken latestToken, List<JSONToken> encapsulatingTokens) {
        boolean insideArray = false;

        if (latestToken == OBJECT_BEGIN) {
            encapsulatingTokens.add(OBJECT_BEGIN);
        } else if (latestToken == ARRAY_BEGIN) {
            encapsulatingTokens.add(ARRAY_BEGIN);
        } else if (latestToken == OBJECT_END || latestToken == ARRAY_END) {
            encapsulatingTokens.remove(encapsulatingTokens.size() - 1);
        }

        if (encapsulatingTokens.size() > 0 &&
                encapsulatingTokens.get(encapsulatingTokens.size() - 1) == ARRAY_BEGIN) {
            insideArray = true;
        }

        return insideArray;
    }
}