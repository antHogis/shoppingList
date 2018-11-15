package com.github.anthogis.shoppinglist;

import com.github.anthogis.json_parser.JSONAttribute;
import com.github.anthogis.json_parser.JSONObject;
import com.github.anthogis.json_parser.JSONWriter;

import java.io.IOException;

public class ParserInterface {
    private JSONWriter jsonWriter;
    private JSONObject shoppingList;

    public ParserInterface() {
        shoppingList = new JSONObject();
    }

    public void addShoppingItem(ShoppingListItem item) {
        int itemAmount = Integer.parseInt(item.getItemAmount());
        shoppingList.addAttribute(new JSONAttribute<Integer>(item.getItemName(), itemAmount));
    }

    public void clearShoppingList() {
        shoppingList = new JSONObject();
    }

    public boolean writeToJSON(String fileName) {
        boolean writeSuccessful = false;
        shoppingList.formatObject();

        try {
            jsonWriter = new JSONWriter(shoppingList.getNotation(), fileName);
            writeSuccessful = jsonWriter.writeFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return writeSuccessful;
    }
}
