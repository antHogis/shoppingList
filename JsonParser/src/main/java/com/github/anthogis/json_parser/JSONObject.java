package com.github.anthogis.json_parser;

import java.util.ArrayList;
import java.util.List;

/**
 * An object that can be passed to JSONWriter to create a JSON file.
 *
 * <p>An object that can be passed to JSONWriter to create a JSON file. Contains a list of JSONAttributes, and
 * can form the JSON notation for the object on a single line.</p>
 *
 * @author antHogis
 * @version 1.3
 * @since 1.0
 */
public class JSONObject implements JSONContainer{

    /**
     * List of the attributes of this object.
     */
    private final List<JSONAttribute> attributes;

    /**
     * The JSON notation of this object.
     */
    private String notation;

    /**
     * The constructor for JSONObject.
     */
    public JSONObject() {
        this.attributes = new ArrayList<>();
        this.notation = "";
    }

    /**
     * Adds a JSONAttribute to JSONObject's list of JSONAttributes.
     * @param attribute the JSONAttribute to be added.
     */
    @Override
    public void add(JSONAttribute attribute) {
        attributes.add(attribute);
    }

    /**
     * Creates the JSON notation for this JSONObject.
     */
    public void formatObject() {
        StringBuilder builder = new StringBuilder();

        builder.append('{');
        for (JSONAttribute attribute : attributes) {
            builder.append(attribute.getNotation()).append(',');
        }

        builder.deleteCharAt(builder.length() - 1);
        builder.append('}');

        notation = builder.toString();
    }

    /**
     * Returns the list of JSONAttributes.
     * @return the list of JSONAttributes.
     */
    @Override
    public List<JSONAttribute> getValues() {
        return attributes;
    }

    /**
     * Returns the JSON notation of this JSONObject.
     * @return the JSON notation.
     */
    public String getNotation() {
        return notation;
    }
}
