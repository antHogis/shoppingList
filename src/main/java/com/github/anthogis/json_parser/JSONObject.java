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
        this.attributes = new ArrayList<>();
        this.notation = "";
    }

    /**
     * Adds a JSONAttribute to JSONObject's list of JSONAttributes.
     * @param attribute the JSONAttribute to be added.
     */
    public void addAttribute(JSONAttribute attribute) {
        attributes.add(attribute);
    }

    /**
     * Creates the JSON notation for this JSONObject.
     */
    public void formatObject() {
        StringBuilder builder = new StringBuilder();

        builder.append('{');
        for (JSONAttribute attribute : attributes) {
            builder.append(attribute.getNotation() + ',');
        }

        builder.deleteCharAt(builder.length() - 1);
        builder.append('}');

        notation = builder.toString();
    }

    /**
     * Returns the list of JSONAttributes.
     * @return the list of JSONAttributes.
     */
    public List<JSONAttribute> getAttributes() {
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
