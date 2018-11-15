package com.github.anthogis.json_parser;

import java.util.ArrayList;
import java.util.List;

public class JSONObject {
    private String keyWord;
    private List<JSONAttribute> attributes;
    private String notation;

    public JSONObject() {
        this.keyWord = null;
        this.attributes = new ArrayList<>();
        this.notation = "";
    }

    public JSONObject(String keyWord) {
        this.keyWord = keyWord;
        this.attributes = new ArrayList<>();
        this.notation = "";
    }

    public <T> void addAttribute(String keyWord, T value) {
        attributes.add(new JSONAttribute<T>(keyWord, value));
    }

    public void addAttribute(JSONAttribute attribute) {
        attributes.add(attribute);
    }


    public void formatObject() {
        if (keyWord != null) {
            notation += "\"" + keyWord + "\" : ";
        }

        notation += '{';
        for (JSONAttribute attribute : attributes) {
            notation += attribute.getNotation() + ',';
        }

        notation = notation.substring(0, notation.length() - 1);
        notation += '}';
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public List<JSONAttribute> getAttributes() {
        return attributes;
    }

    public String getNotation() {
        return notation;
    }
}
