package com.github.anthogis.json_parser;

import java.util.List;

public class JSONObject {
    private String keyWord;
    private List<JSONAttribute> attributes;

    public JSONObject() {
    }

    public JSONObject(String keyWord) {
        this.keyWord = keyWord;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public List<JSONAttribute> getAttributes() {
        return attributes;
    }
}
