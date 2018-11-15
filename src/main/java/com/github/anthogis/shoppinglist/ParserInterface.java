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

    public void addShoppingItem(String item, int amount) {
        shoppingList.addAttribute(item, amount);
    }

    public void clearShoppingList() {
        shoppingList = new JSONObject();
    }

    public boolean writeToJSON() {
        boolean writeSuccessful = false;
        shoppingList.formatObject();

        try {
            jsonWriter = new JSONWriter(shoppingList.getNotation(), "test");
            writeSuccessful = jsonWriter.writeFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return writeSuccessful;
    }
}
