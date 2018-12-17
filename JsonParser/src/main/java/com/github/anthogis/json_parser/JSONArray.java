package com.github.anthogis.json_parser;

import java.util.ArrayList;
import java.util.List;

class JSONArray implements JSONContainer {
    List<JSONAttribute> attributes;

    public JSONArray() {
        attributes = new ArrayList<>();
    }

    @Override
    public void add(JSONAttribute attribute) {
        attributes.add(attribute);
    }

    @Override
    public List<JSONAttribute> getAttributes() {
        return attributes;
    }
}