package com.github.anthogis.json_parser.api;

import com.github.anthogis.json_parser.utils.JSONContainer;
import com.github.anthogis.json_parser.utils.JSONParseException;
import com.github.anthogis.json_parser.utils.JSONToken;
import com.github.anthogis.json_parser.utils.JSONTokenizer;
import com.github.anthogis.json_parser.utils.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.github.anthogis.json_parser.utils.JSONToken.*;

/**
 * Parses a JSON file.
 *
 * Creates a structure of {@link JSONAttribute}s within a {@link JSONObject} from a JSON file.
 *
 * @author antHogis
 * @version 1.3
 * @since 1.2
 */
public class JSONParser {
    /**
     * The object parsed from a JSON file.
     */
    private JSONContainer parsedContainer;

    /**
     * Constructs a JSONParser, which parses JSON data from a given file.
     *
     * @param filePath the filepath/name of the JSON file to parse.
     * @throws IOException if the JSON file could not be read.
     * @throws JSONParseException if the syntax of the given JSON file contains errors.
     */
    public JSONParser(String filePath) throws IOException, JSONParseException {
        List<String> jsonLines = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        while ((line = reader.readLine()) != null) {
            jsonLines.add(line);
        }
        reader.close();

        List<Pair<JSONToken, String>> jsonTokens
                = new JSONTokenizer(jsonLines).tokenize().getTokens();
        inspectTokenSyntax(jsonTokens);
        parsedContainer = parseContainer(jsonTokens);
    }

    /**
     * Returns the object parsed from the JSON file.
     * @return the object parsed from the JSON file.
     */
    public JSONContainer getParsedContainer() {
        return parsedContainer;
    }

    /**
     * Verifies that the syntax of a list of JSONTokens is in order.
     *
     * @param jsonTokens the list of JSONTokens to inspect.
     * @throws JSONParseException if the syntax of the JSONTokens is not in order.
     */
    private void inspectTokenSyntax(List<Pair<JSONToken, String>> jsonTokens)
            throws JSONParseException {
        List<JSONToken> expectedTokens = new ArrayList<>();
        //For a structure of which is the active encapsulating token (ARRAY_BEGIN/OBJECT_BEGIN)
        List<JSONToken> containerTokens = new ArrayList<>(jsonTokens.size());
        expectedTokens.add(OBJECT_BEGIN);
        expectedTokens.add(ARRAY_BEGIN);

        /* For debugging
        for (int i = 0; i < jsonTokens.size(); i++) {
            String token = jsonTokens.get(i).getSecond().equals("") ?
                    jsonTokens.get(i).getFirst().name() :
                    jsonTokens.get(i).getFirst().name() + " | " + jsonTokens.get(i).getSecond();
            System.out.println(i + ". " + token);
        }
        */

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

            boolean insideArray = isInsideArray(token, containerTokens);

            /*
            //PRINT FOR DEBUG
            String tokenString = jsonTokens.get(i).getSecond().equals("") ?
                    token.name() : token.name() + " | " + jsonTokens.get(i).getSecond();
            System.out.printf("%d. %s: %b\n", i, tokenString, insideArray);
            */

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
                    expectedTokens = new ArrayList<>(expectedTokens);
                    expectedTokens.add(ARRAY_END);
                    break;
                case ARRAY_END:
                    expectedTokens = expectAfterValue(insideArray);
                    break;
                case INTEGER:
                case FLOAT:
                case STRING:
                case BOOLEAN:
                case NULL:
                    expectedTokens = expectAfterValue(insideArray);
                    break;
            }

        }
    }

    /**
     * Parses a {@link JSONContainer} from a list of pairs of JSONTokens and values.
     *
     * @param jsonTokens the pairs of JSONTokens and values.
     * @return the container of values.
     */
    private JSONContainer parseContainer(List<Pair<JSONToken, String>> jsonTokens) {
        JSONContainer container = null;

        String key = "";
        boolean isOuterObject = false;
        boolean isOuterArray = false;

        if (jsonTokens.get(0).getFirst() == OBJECT_BEGIN) {
            isOuterObject = true;
        } else if (jsonTokens.get(0).getFirst() == ARRAY_BEGIN) {
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
                        attribute = new JSONAttribute<>(key, parseContainer(nestedObjectTokens));
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
                case FLOAT:
                    attribute = new JSONAttribute<>(key, Double.parseDouble(value));
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
                        attribute = new JSONAttribute<>(key, parseContainer(nestedTokens).getValues());
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
     * Checks if a token is found in a list of tokens.
     *
     * @param actualToken the token that is compared to values of the list.
     * @param expectedTokens the list of tokens that actualToken is expected to be found from.
     * @return true if actualToken was found from the list of expectedTokens.
     */
    private boolean tokenIsExpected(JSONToken actualToken, List<JSONToken> expectedTokens) {
        boolean actualIsExpected = false;

        for (JSONToken expectedToken : expectedTokens) {
            if (actualToken == expectedToken) {
                actualIsExpected = true; break;
            }
        }

        return actualIsExpected;
    }

    /**
     * Returns a list of tokens expected when a value should be encountered.
     *
     * @return a list of tokens expected when a value should be encountered.
     */
    private List<JSONToken> expectValueList() {
        return Arrays.asList(OBJECT_BEGIN, NULL, BOOLEAN, INTEGER, STRING, ARRAY_BEGIN, FLOAT);
    }

    /**
     * Returns a list of tokens expected when a value should be assigned.
     *
     * @return a list of tokens expected when a value should be assigned.
     */
    private List<JSONToken> expectAssignList() {
        return Arrays.asList(ASSIGN);
    }

    /**
     * Returns a list of tokens expected when a key should be encountered.
     *
     * @return a list of tokens expected when a key should be encountered.
     */
    private List<JSONToken> expectKeyList() {
        return Arrays.asList(KEY, OBJECT_END);
    }

    /**
     * Returns a list of tokens expected after a value.
     *
     * @param insideArray whether the value was within an array or not.
     * @return a list of tokens expected after a value.
     */
    private List<JSONToken> expectAfterValue(boolean insideArray)  {
        return insideArray ? Arrays.asList(DELIMITER, ARRAY_END)
                : Arrays.asList(DELIMITER, OBJECT_END);
    }

    /**
     * Returns a list of tokens expected after a delimiter.
     *
     * @param insideArray whether the delimiter was within an array or not.
     * @return a list of tokens expected after a delimiter.
     */
    private List<JSONToken> expectAfterDelimiter(boolean insideArray) {
        return insideArray ? expectValueList() : Arrays.asList(KEY);
    }

    /**
     * Returns the index of the corresponding closing token of a container.
     *
     * @param jsonTokens the list of tokens to search the index of the closing token.
     * @param start the index of the opening token.
     * @param type the type of Container (JSONArray or JSONObject)
     * @return the index of the closing token of a container.
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
     * Returns true if the current position in the list of tokens is contained within an array.
     *
     * Takes the latest produced token, and checks if it is significant in changing the context of the current position
     * in tokens. Modifies the list of container tokens if it is, then checks if the last token in containerTokens
     * signifies that the current position is within an array.
     *
     *
     * @param latestToken the token that was produced last
     * @param containerTokens the list of tokens that signify containers opening or closing.
     * @return true if the current position in the list of tokens is contained within an array.
     */
    private boolean isInsideArray(JSONToken latestToken, List<JSONToken> containerTokens) {
        boolean insideArray = false;

        if (latestToken == OBJECT_BEGIN) {
            containerTokens.add(OBJECT_BEGIN);
        } else if (latestToken == ARRAY_BEGIN) {
            containerTokens.add(ARRAY_BEGIN);
        } else if (latestToken == OBJECT_END || latestToken == ARRAY_END) {
            containerTokens.remove(containerTokens.size() - 1);
        }

        if (containerTokens.size() > 0 &&
                containerTokens.get(containerTokens.size() - 1) == ARRAY_BEGIN) {
            insideArray = true;
        }

        return insideArray;
    }
}