package com.github.anthogis.shoppinglist;

import com.github.anthogis.json_parser.JSONAttribute;
import com.github.anthogis.json_parser.JSONObject;
import com.github.anthogis.json_parser.JSONWriter;

import java.io.IOException;

/**
 * ParserInterface establishes connectivity between <code>MainWindowController</code> and the JSON parser.
 *
 * <p>ParserInterface establishes connectivity between <code>MainWindowController</code> and the JSON parser.
 * ParserInterface holds a <code>JSONObject</code> containing <code>JSONAttribute</code>s, and writes the content of the
 * <code>JSONObject</code> to .json file using a <code>JSONWriter</code>.</p>
 */
public class ParserInterface {
    /**
     * The JSONWriter that writes the .json file
     */
    private JSONWriter jsonWriter;

    /**
     * The JSONObject which contains data received from <code>MainWindowController</code>.
     */
    private JSONObject shoppingList;

    /**
     * The constructor for ParserInterface.
     */
    public ParserInterface() {
        shoppingList = new JSONObject();
    }

    /**
     * Adds a JSONAttribute (with data from a ShoppingListItem) to <code>shoppingList</code>.
     * @param item the ShoppingListItem containing the data.
     */
    public void addShoppingItem(ShoppingListItem item) {
        int itemAmount = Integer.parseInt(item.getItemAmount());
        shoppingList.addAttribute(new JSONAttribute<Integer>(item.getItemName(), itemAmount));
    }

    /**
     * Re-instantiates shoppingList.
     */
    public void clearShoppingList() {
        shoppingList = new JSONObject();
    }

    /**
     * Writes shoppingList to a .json file using JSONWriter.
     * @param fileName the name of the .json file.
     * @return true if the write was successful.
     */
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
