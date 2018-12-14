package com.github.anthogis.json_parser;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO Write javaoc
 *
 * @author antHogis
 * @version 1.3
 * @since 1.2
 */
public class JSONTokenizer {
    List<String> data;
    ArrayList<Pair<JSONToken, String>> tokens;

    public JSONTokenizer(List<String> data) {
        this.data = data;
        tokens = new ArrayList<>();
    }

    public JSONTokenizer tokenize() throws JSONParseException {
        boolean storeString = false;
        boolean storeNumber = false;
        boolean storeBoolean = false;
        boolean storeNull = false;
        boolean storeKey = false;
        boolean putValue = false;
        boolean delimiterFound = false;
        boolean objectCloseFound = false;
        boolean arrayCloseFound = false;
        StringBuilder value = new StringBuilder();

        final int zeroChar = 48;
        final int nineChar = 57;

        for (int i = 0; i < data.size(); i++) {
            String currentLine = data.get(i);
            for (int j = 0; j < currentLine.length(); j++) {
                char currentChar = currentLine.charAt(j);

                if (currentChar == '{' && !storeString) {
                    tokens.add(new Pair<>(JSONToken.OBJECT_BEGIN, ""));
                } else if (currentChar == ':' && !storeString) {
                    tokens.add(new Pair<>(JSONToken.ASSIGN, ""));
                } else if (currentChar >= zeroChar && currentChar <= nineChar) {
                    if (!storeString) {
                        storeNumber = true;
                    }
                    value.append(currentChar);
                } else if (currentChar == '\"') {
                    if (expectKey() && !storeKey) {
                        storeKey = true;
                    } else if (storeKey) {
                        putValue = true;
                    } else if (storeString) {
                        putValue = true;
                    } else {
                        storeString = true;
                    }
                } else if (Character.isLetter(currentChar)) {
                    if (storeString || storeKey) {
                        value.append(currentChar);
                    } else {
                        try {
                            String nextChars = currentLine.substring(j, j + 4);

                            if (nextChars.equals("true")) {
                                storeBoolean = true;
                                putValue = true;
                                value.append(nextChars);
                                j += 3;
                            } else if (nextChars.equals("null") && !putValue) {
                                storeNull = true;
                                putValue = true;
                                value.append(nextChars);
                                j += 3;
                            }

                            if (!putValue) {
                                nextChars = currentLine.substring(j, j + 5);

                                if (nextChars.equals("false")) {
                                    storeBoolean = true;
                                    putValue = true;
                                    value.append(nextChars);
                                    j += 4;
                                } else {
                                    throw new JSONParseException(i + 1, j + 1);
                                }
                            }
                        } catch (IndexOutOfBoundsException e) {
                            throw new JSONParseException(i + 1, j + 1);
                        }
                    }
                } else if (currentChar == ',') {
                    putValue = true;
                    delimiterFound = true;
                } else if (currentChar == '}') {
                    putValue = true;
                    objectCloseFound = true;
                } else if (currentChar == ']') {
                    putValue = true;
                    arrayCloseFound = true;
                } else if (currentChar == '[') {
                    tokens.add(new Pair<>(JSONToken.ARRAY_BEGIN, ""));
                } else if (!isIgnorableChar(currentChar)) {
                    //If character is unexpected, throw
                    throw new JSONParseException(i + 1, j + 1);
                }

                if (putValue) {
                    putValue = false;

                    if (storeString) {
                        storeString = false;
                        tokens.add(new Pair<>(JSONToken.STRING, value.toString()));
                    } else if (storeNumber) {
                        storeNumber = false;
                        tokens.add(new Pair<>(JSONToken.INTEGER, value.toString()));
                    } else if (storeBoolean) {
                        storeBoolean = false;
                        tokens.add(new Pair<>(JSONToken.BOOLEAN, value.toString()));
                    } else if (storeNull) {
                        storeNull = false;
                        tokens.add(new Pair<>(JSONToken.NULL, value.toString()));
                    } else if (storeKey) {
                        storeKey = false;
                        tokens.add(new Pair<>(JSONToken.KEY, value.toString()));
                    }


                    if (delimiterFound) {
                        delimiterFound = false;
                        tokens.add(new Pair<>(JSONToken.DELIMITER, ""));
                    } else if (objectCloseFound) {
                        objectCloseFound = false;
                        tokens.add(new Pair<>(JSONToken.OBJECT_END, ""));
                    } else if (arrayCloseFound) {
                        arrayCloseFound = false;
                        tokens.add(new Pair<>(JSONToken.ARRAY_END, ""));
                    }

                    value = new StringBuilder();
                }
            }
            System.out.println();
        }

        return this;
    }

    private boolean isIgnorableChar(char c) {
        return c == '\b' || c == '\f' || c == '\n' || c == '\r'
                || c == '\t' || c == ' ';
    }

    private boolean expectKey() {
        boolean expectKey = false;

        try {
            JSONToken lastToken = tokens.get(tokens.size() - 1).getFirst();
            expectKey = lastToken == JSONToken.OBJECT_BEGIN || lastToken == JSONToken.DELIMITER;

            for (int i = tokens.size() - 1; i >= 0; i--) {
                if (tokens.get(i).getFirst() == JSONToken.ARRAY_BEGIN) {
                    expectKey = false;
                    break;
                } else if (tokens.get(i).getFirst() == JSONToken.ARRAY_END) {
                    break;
                }
            }
        } catch (IndexOutOfBoundsException e) {}

        return expectKey;
    }

    public ArrayList<Pair<JSONToken, String>> getTokens() {
        return tokens;
    }
}
