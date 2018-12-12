package com.github.anthogis.json_parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        expectedTokens.add(JSONToken.OBJECT_BEGIN);

        JSONTokenizer tokenizer = new JSONTokenizer(jsonLines);

        tokenizer.tokenize();

        for (Pair<JSONToken, String> pair : tokenizer.getTokens()) {
            if (!tokenIsExpected(pair.getKey(), expectedTokens)) {
                throw new JSONParseException();
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
        return Arrays.asList(JSONToken.OBJECT_BEGIN,
                JSONToken.NULL,
                JSONToken.BOOLEAN,
                JSONToken.NUMBER,
                JSONToken.STRING);
    }

    private List<JSONToken> expectAssignList() {
        return Arrays.asList(JSONToken.ASSIGN);
    }
}
