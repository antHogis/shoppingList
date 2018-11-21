package com.github.anthogis.json_parser;

public class JSONParseException extends  RuntimeException {
    public JSONParseException() {
        super("Error in parsing JSON");
    }

    public JSONParseException(String message) {
        super(message);
    }

    public JSONParseException(int row, int col) {
        super(String.format("Illegal character in json file at [%d, %d]", row, col));
    }
}
