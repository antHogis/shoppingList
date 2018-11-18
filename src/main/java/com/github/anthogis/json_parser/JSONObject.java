package com.github.anthogis.json_parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * An object that can be passed to JSONWriter to create a JSON file.
 *
 * <p>An object that can be passed to JSONWriter to create a JSON file. Contains a list of JSONAttributes, and
 * can form the JSON notation for the object on a single line.</p>
 *
 * @author antHogis
 * @version 1.0
 * @since 1.0
 */
public class JSONObject {
    /**
     * Keyword of the object. Should be present if this object is the JSONAttribute of another JSONObject.
     */
    private Optional<String> keyWord;

    /**
     * List of the attributes of this object.
     */
    private List<JSONAttribute> attributes;

    /**
     * The JSON notation of this object.
     */
    private String notation;

    /**
     * The constructor for JSONObject.
     */
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
