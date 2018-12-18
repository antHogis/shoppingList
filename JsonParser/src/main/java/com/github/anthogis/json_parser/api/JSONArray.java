package com.github.anthogis.json_parser.api;

import com.github.anthogis.json_parser.api.JSONAttribute;
import com.github.anthogis.json_parser.api.JSONParser;
import com.github.anthogis.json_parser.utils.JSONContainer;

import java.util.ArrayList;
import java.util.List;

/**
 * A representation of an array in JSON notation.
 *
 * This class represents an array in JSON notation, but is intended for internal use only. It is meant to be used
 * in conjunction with {@link JSONParser}, to allow parsing to be done by the same
 * method recursively for both arrays and objects.
 *
 * @author antHogis
 * @version 1.3
 * @since 1.3
 */
public class JSONArray implements JSONContainer {

    /**
     * The list of JSONAttributes within the array.
     */
    private List<JSONAttribute> values;

    /**
     * Constructs a JSONArray with a list of JSONAttributes.
     */
    public JSONArray() {
        values = new ArrayList<>();
    }

    /**
     * Adds a JSONAttribute to values.
     *
     * @param value the attribute to add
     */
    @Override
    public void add(JSONAttribute value) {
        values.add(value);
    }

    /**
     * Returns the list of values
     *
     * @return the list of values
     */
    @Override
    public List<JSONAttribute> getValues() {
        return values;
    }

    @Override
    public String getNotation() {
        StringBuilder notationBuilder = new StringBuilder();

        notationBuilder.append('[');

        notationBuilder.append(']');

        return notationBuilder.toString();
    }
}