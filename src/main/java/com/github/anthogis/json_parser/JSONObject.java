package com.github.anthogis.json_parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JSONObject {
    private Optional<String> keyWord;
    private List<JSONAttribute> attributes;
    private String notation;

    public JSONObject() {
        this.keyWord = Optional.empty();
        this.attributes = new ArrayList<>();
        this.notation = "";
    }

    public JSONObject(String keyWord) {
        this.keyWord = Optional.of(keyWord);
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
        StringBuilder builder = new StringBuilder();

        keyWord.ifPresent(key -> builder.append('"' + key + "\" : "));

        builder.append('{');
        for (JSONAttribute attribute : attributes) {
            builder.append(attribute.getNotation() + ',');
        }

        builder.deleteCharAt(builder.length() - 1);
        builder.append('}');

        notation = builder.toString();
    }

    public List<JSONAttribute> getAttributes() {
        return attributes;
    }

    public String getNotation() {
        return notation;
    }
}
