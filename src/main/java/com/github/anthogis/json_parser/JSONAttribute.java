package com.github.anthogis.json_parser;

import java.util.Collection;
import java.util.Iterator;

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
                notation += iterator.next().toString() + ",";
            }
            notation = notation.substring(0, notation.length() - 1);
            notation += "]";
        } else if (value instanceof JSONObject) {
            ((JSONObject) value).formatObject();
            notation += ((JSONObject) value).getNotation();
        } else if (value == null) {
            notation += "null";
        } else if (value instanceof Number || value instanceof Boolean) {
            notation += value.toString();
        } else {
            notation += "\"" + value.toString() + "\"";
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
