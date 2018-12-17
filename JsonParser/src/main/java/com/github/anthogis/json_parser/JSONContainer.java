package com.github.anthogis.json_parser;

import java.util.List;

/**
 * TODO
 */
public interface JSONContainer {

    /**
     * TODO
     * @param attribute
     */
    void add(JSONAttribute attribute);

    /**
     * TODO
     * @return
     */
    List<JSONAttribute> getAttributes();
}
