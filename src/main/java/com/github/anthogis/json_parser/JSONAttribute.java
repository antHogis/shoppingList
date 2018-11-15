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
            notation += '[';
            if (((Collection) value).isEmpty()) {
                notation += ' ';
            } else {
                Iterator iterator = ((Collection) value).iterator();
                while(iterator.hasNext()) {
                    notation += formatByType(iterator.next()) + ",";
                }
                notation = notation.substring(0, notation.length() - 1);
            }
            notation += ']';
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

    private String formatByType(Object value) {
        String formatted;

        if (value == null) {
            formatted = "null";
        } else if (value instanceof Number || value instanceof Boolean) {
            formatted = value.toString();
        } else {
            formatted = "\"" + value.toString() + "\"";
        }

        return formatted;
    }

    public String getNotation() {
        return notation;
    }

    @Override
    public String toString() {
        return notation;
    }
}
