package com.github.anthogis.json_parser;

/**
 * TODO Write javadoc
 *
 * @author antHogis
 * @version 1.3
 * @since 1.2
 */
public enum JSONToken {
    OBJECT_BEGIN,
    OBJECT_END,
    ARRAY_BEGIN,
    ARRAY_END,
    ASSIGN,
    KEY,
    DELIMITER,
    STRING,
    INTEGER,
    BOOLEAN,
    NULL
}
