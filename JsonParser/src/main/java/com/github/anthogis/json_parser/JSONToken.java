package com.github.anthogis.json_parser;

public enum JSONToken {
    OBJECT_BEGIN,
    OBJECT_END,
    ARRAY_BEGIN,
    ARRAY_END,
    ASSIGN,
    KEY,
    DELIMITER,
    STRING,
    NUMBER,
    BOOLEAN,
    NULL
}
