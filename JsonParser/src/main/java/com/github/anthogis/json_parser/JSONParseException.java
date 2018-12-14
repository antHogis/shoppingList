package com.github.anthogis.json_parser;

/**
 * An exception thrown if there's a syntax error in a parsed JSON file.
 */
public class JSONParseException extends  RuntimeException {

    /**
     * Constructs a JSONParseException with a custom message.
     *
     * @param message the custom message.
     */
    public JSONParseException(String message) {
        super(message);
    }

    /**
     * Constructs a JSONParseException that informs on which row and column of a JSON file the exception was encountered.
     *
     * @param row the row where the exception was encountered.
     * @param col the column where the exception was encountered.
     */
    public JSONParseException(int row, int col) {
        super(String.format("Illegal character in json file at [%d, %d]", row, col));
    }
}
