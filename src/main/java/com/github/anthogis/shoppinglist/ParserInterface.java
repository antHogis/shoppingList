package com.github.anthogis.shoppinglist;

import com.github.anthogis.json_parser.JSONObject;
import com.github.anthogis.json_parser.JSONWriter;

import java.io.IOException;

public class ParserInterface {
    private JSONWriter jsonWriter;
    private JSONObject shoppingList;

    public ParserInterface() {
        shoppingList = new JSONObject();
    }
}
