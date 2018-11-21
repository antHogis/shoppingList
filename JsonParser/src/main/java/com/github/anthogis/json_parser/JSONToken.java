package com.github.anthogis.json_parser;

public enum JSONToken {
    OBJECT_OPEN,
    OBJECT_CLOSE,
    ARRAY_OPEN,
    ARRAY_CLOSE,
    ASSIGN,
    DELIMITER,
    STRING,
    NUMBER,
    BOOLEAN,
    NULL
}
