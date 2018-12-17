package com.github.anthogis.json_parser;

import java.util.ArrayList;
import java.util.List;

/**
 * A representation of an array in JSON notation.
 *
 * This class represents an array in JSON notation, but is intended for internal use only. It is meant to be used
 * in conjunction with {@link com.github.anthogis.json_parser.JSONParser}, to allow parsing to be done by the same
 * method recursively for both arrays and objects.
 *
 * @author antHogis
 * @version 1.3
 * @since 1.3
 */
class JSONArray implements JSONContainer {

    /**
     * The list of JSONAttributes within the array.
     */
    private List<JSONAttribute> attributes;

    /**
     * Constructs a JSONArray with a list of JSONAttributes.
     */
    public JSONArray() {
        attributes = new ArrayList<>();
    }

    /**
     * Adds a JSONAttribute to attributes.
     *
     * @param attribute the attribute to add
     */
    @Override
    public void add(JSONAttribute attribute) {
        attributes.add(attribute);
    }

    /**
     * Returns the list of attributes
     *
     * @return the list of attributes
     */
    @Override
    public List<JSONAttribute> getAttributes() {
        return attributes;
    }
}