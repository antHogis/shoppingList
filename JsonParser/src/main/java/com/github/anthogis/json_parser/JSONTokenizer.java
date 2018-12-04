package com.github.anthogis.json_parser;

import java.util.ArrayList;
import java.util.List;

public class JSONTokenizer {
    List<String> data;
    ArrayList<Pair<JSONToken, String>> tokens;

    public JSONTokenizer(List<String> data) {
        this.data = data;
        tokens = new ArrayList<>();
    }

    public void tokenize() throws JSONParseException {
        boolean storeString = false;
        boolean storeNumber = false;
        boolean storeBoolean = false;
        boolean storeNull = false;
        boolean putValue = false;
        boolean delimiterFound = false;
        boolean objectCloseFound = false;
        StringBuilder value = new StringBuilder();

        final int zeroChar = 48;
        final int nineChar = 57;

        for (int i = 0; i < data.size(); i++) {
            String currentLine = data.get(i);
            for (int j = 0; j < currentLine.length(); j++) {
                char currentChar = currentLine.charAt(j);
                System.out.print(currentChar);

                if (currentChar == '{' && !storeString) {
                    tokens.add(new Pair(JSONToken.OBJECT_BEGIN, ""));
                } else if (currentChar == ':' && !storeString) {
                    tokens.add(new Pair(JSONToken.ASSIGN, ""));
                } else if (currentChar >= zeroChar && currentChar <= nineChar) {
                    if (!storeString) {
                        storeNumber = true;
                    }
                    value.append(currentChar);
                } else if (currentChar == '\"') {
                    if (storeString) {
                        putValue = true;
                    } else {
                        storeString = true;
                    }
                } else if (Character.isLetter(currentChar)) {
                    if (storeString) {
                        value.append(currentChar);
                    } else if (j + 4 < currentLine.length()) {
                        String nextFourChars = currentLine.substring(j, j + 4);
                        if (nextFourChars.matches("true|false")) {
                            storeBoolean = true;
                            putValue = true;
                            value.append(nextFourChars);
                            j += 4;
                        } else if (nextFourChars.equals("null")) {
                            storeNull = true;
                            putValue = true;
                            value.append(nextFourChars);
                            j += 4;
                        } else {
                            throw new JSONParseException(i, j);
                        }
                    }
                } else if (currentChar == ',') {
                    putValue = true;
                    delimiterFound = true;
                } else if (currentChar == '}') {
                    putValue = true;
                    objectCloseFound = true;
                } else if (!isIgnorableChar(currentChar)) {
                    throw new JSONParseException(i, j);
                }

                if (putValue) {
                    putValue = false;

                    if (storeString) {
                        storeString = false;
                        tokens.add(new Pair(JSONToken.STRING, value.toString()));
                    } else if (storeNumber) {
                        storeNumber = false;
                        tokens.add(new Pair(JSONToken.NUMBER, value.toString()));
                    } else if (storeBoolean) {
                        storeBoolean = false;
                        tokens.add(new Pair(JSONToken.BOOLEAN, value.toString()));
                    } else if (storeNull) {
                        storeNull = false;
                        tokens.add(new Pair(JSONToken.NULL, value.toString()));
                    }



                    if (delimiterFound) {
                        delimiterFound = false;
                        tokens.add(new Pair(JSONToken.DELIMITER, ""));
                    } else if (objectCloseFound) {
                        objectCloseFound = false;
                        tokens.add(new Pair(JSONToken.OBJECT_END, ""));
                    }

                    value = new StringBuilder();
                }

            }
            System.out.println();
        }
    }

    boolean isIgnorableChar(char c) {
        return c == '\b' || c == '\f' || c == '\n' || c == '\r'
                || c == '\t' || c == ' ';
    }

    public ArrayList<Pair<JSONToken, String>> getTokens() {
        return tokens;
    }
}
