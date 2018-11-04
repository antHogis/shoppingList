package com.github.anthogis.json_parser;

import java.util.Collection;

public class JSONAttribute<T> {
    private String keyWord;
    private T value;
    private String notation;

    public JSONAttribute(String keyWord, T value) {
        this.keyWord = keyWord;
        this.value = value;
        formatAttribute();
    }

    public void formatAttribute() {
        //notation = '\"' + keyWord + "\": ";

        if (value instanceof Collection) {
            notation = "array";
        } else if (value instanceof Number || value instanceof Boolean || value == null) {
            notation = "no quotes";
        } else {
            notation = "as string";
        }
    }

}
