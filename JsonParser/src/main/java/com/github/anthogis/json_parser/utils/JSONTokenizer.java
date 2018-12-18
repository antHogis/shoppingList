package com.github.anthogis.json_parser.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Creates {@link JSONToken}s from lines of JSON data.
 *
 * @author antHogis
 * @version 1.3
 * @since 1.2
 */
public class JSONTokenizer {
    /**
     * Lines of JSON data given to tokenize.
     */
    List<String> data;

    /**
     * List of pairs of tokens and values tokenized by this class.
     */
    ArrayList<Pair<JSONToken, String>> tokens;

    /**
     * Constructs a JSONTokenizer that contains data to tokenize.
     *
     * @param data the data to tokenize.
     */
    public JSONTokenizer(List<String> data) {
        this.data = data;
        tokens = new ArrayList<>();
    }

    /**
     * Tokenizes the json data.
     *
     * @return the instance of this JSONTokenizer.
     * @throws JSONParseException if an error was encountered in the syntax of given data.
     */
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
                    if (!storeString && !storeKey) {
                        storeNumber = true;
                    }
                    value.append(currentChar);
                } else if (currentChar == '\"') {
                    try {
                        if (expectKey() && !storeKey) {
                            storeKey = true;
                        } else if (storeKey) {
                            putValue = true;
                        } else if (storeString) {
                            putValue = true;
                        } else {
                            storeString = true;
                        }
                    } catch (IndexOutOfBoundsException e) {
                        throw  new JSONParseException(i + 1, j + 1);
                    }
                } else if (Character.isLetter(currentChar) || storeString || storeKey) {
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
                                    for (Pair<JSONToken, String> token : tokens) {
                                        System.out.println(token.getFirst().name());
                                    }
                                    throw new JSONParseException(i + 1, j + 1);
                                }
                            }
                        } catch (IndexOutOfBoundsException e) {
                            throw new JSONParseException(i + 1, j + 1);
                        }
                    }
                } else if (storeNumber && currentChar == '.') {
                    value.append(currentChar);
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
                        if (value.toString().contains(".")) {
                            tokens.add(new Pair<>(JSONToken.FLOAT, value.toString()));
                        } else {
                            tokens.add(new Pair<>(JSONToken.INTEGER, value.toString()));
                        }
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
        }

        return this;
    }

    /**
     * Inspects if a character is an ignorable character.
     *
     * Inspects if a character is an ignorable character, like a whitespace or control character.
     *
     * @param c the character to inspect
     * @return true if the inspected character can be ignored.
     */
    private boolean isIgnorableChar(char c) {
        return c == '\b' || c == '\f' || c == '\n' || c == '\r'
                || c == '\t' || c == ' ';
    }

    /**
     * Returns true if a KEY token should be expected next. Used to determine whether to store an encountered string as a key or value.
     *
     * @return true if a KEY token should be expected next.
     * @throws IndexOutOfBoundsException if tokens is empty
     */
    private boolean expectKey() throws IndexOutOfBoundsException {
        return tokens.get(tokens.size() - 1).getFirst() != JSONToken.ASSIGN
                && !isInsideArray();
    }

    /**
     * Returns the list of tokens created from the given json data.
     *
     * @return the list of tokens created from the given json data.
     */
    public ArrayList<Pair<JSONToken, String>> getTokens() {
        return tokens;
    }

    /**
     * Returns true if the data encountered is within an array.
     *
     * @return true if the data encountered is within an array.
     */
    private boolean isInsideArray() {
        int openobjects = 0; int openarrays = 0;

        for (int i = tokens.size() - 1; i >= 0; i--) {
            JSONToken token = tokens.get(i).getFirst();

            if (token == JSONToken.OBJECT_BEGIN) openobjects++;
            else if (token == JSONToken.OBJECT_END) openobjects--;
            else if (token == JSONToken.ARRAY_BEGIN) openarrays++;
            else if (token == JSONToken.ARRAY_END) openarrays--;

            if (token == JSONToken.ARRAY_BEGIN && openarrays > 0) {
                return true;
            } else if (token == JSONToken.OBJECT_BEGIN && openobjects > 0) {
                return false;
            }
        }

        return false;
    }
}
