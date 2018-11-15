package com.github.anthogis.json_parser;

import java.util.Collection;
import java.util.Iterator;

import static com.github.anthogis.json_parser.JSONWriter.indent;

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
        notation = '\"' + keyWord + "\" : ";

        if (value instanceof Collection) {
            notation += "[";
            Iterator iterator = ((Collection) value).iterator();
            while(iterator.hasNext()) {
                notation += iterator.next().toString();
            }
            notation += "]";
        } else if (value instanceof Number || value instanceof Boolean || value == null) {
            notation = "no quotes";
        } else {
            notation = "as string";
        }
    }

    public String getNotation() {
        return notation;
    }

    @Override
    public String toString() {
        return notation;
    }
}
