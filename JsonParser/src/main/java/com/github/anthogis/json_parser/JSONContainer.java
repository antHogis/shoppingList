package com.github.anthogis.json_parser;

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
}
