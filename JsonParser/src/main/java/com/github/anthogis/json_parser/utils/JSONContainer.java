package com.github.anthogis.json_parser.utils;

import com.github.anthogis.json_parser.api.JSONAttribute;

import java.util.List;

/**
 * Contains JSON values.
 *
 * A JSONContainer contains JSON values.
 */
public interface JSONContainer {

    /**
     * Adds a value to the container.
     *
     * @param value the value to add.
     */
    void add(JSONAttribute value);

    /**
     * Returns the list of values.
     *
     * @return the list of values.
     */
    List<JSONAttribute> getValues();

    /**
     * Returns the JSON notation of this container.
     * @return the JSON notation of this container.
     */
    String getNotation();
}
