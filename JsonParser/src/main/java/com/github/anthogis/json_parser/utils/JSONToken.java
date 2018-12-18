package com.github.anthogis.json_parser.utils;

import com.github.anthogis.json_parser.api.JSONParser;

/**
 * Tokens for parsing JSON.
 *
 * Contains tokens that a {@link JSONParser} can parse.
 *
 * @author antHogis
 * @version 1.3
 * @since 1.2
 */
public enum JSONToken {
    /**
     * Token for when an object begins.
     */
    OBJECT_BEGIN,

    /**
     * Token for when an object ends.
     */
    OBJECT_END,

    /**
     * Token for when an array begins.
     */
    ARRAY_BEGIN,

    /**
     * Token for when an array ends.
     */
    ARRAY_END,

    /**
     * Token for when an assign symbol is encountered.
     */
    ASSIGN,

    /**
     * Token for a key to a value.
     */
    KEY,

    /**
     * Token for a delimiter between values.
     */
    DELIMITER,

    /**
     * Token for a string value.
     */
    STRING,

    /**
     * Token for an integer value.
     */
    INTEGER,

    /**
     * Token for a floating point value.
     */
    FLOAT,

    /**
     * Token for a boolean value.
     */
    BOOLEAN,

    /**
     * Token for a null value.
     */
    NULL
}
