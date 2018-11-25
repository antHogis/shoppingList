package com.github.anthogis.shoppinglist;

import com.github.anthogis.json_parser.JSONAttribute;
import com.github.anthogis.json_parser.JSONObject;
import com.github.anthogis.json_parser.JSONWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    private JSONObject shoppingListObject;

    private List<JSONObject> shoppingList;

    /**
     * The constructor for ParserInterface.
     */
    public ParserInterface() {
        shoppingListObject = new JSONObject();
        shoppingList = new ArrayList();
    }

    /**
     * Adds a JSONAttribute (with data from a ShoppingListItem) to <code>shoppingListObject</code>.
     * @param item the ShoppingListItem containing the data.
     */
    public void addShoppingItem(ShoppingListItem item) {
        int itemAmount = Integer.parseInt(item.getItemAmount());
        JSONObject itemObject = new JSONObject();
        itemObject.addAttribute(new JSONAttribute("productName", item.getItemName()));
        itemObject.addAttribute(new JSONAttribute("productAmount", itemAmount));
        shoppingList.add(itemObject);
        //shoppingListObject.addAttribute(new JSONAttribute<Integer>(item.getItemName(), itemAmount));
    }

    /**
     * Re-instantiates shoppingListObject.
     */
    public void clearShoppingList() {
        shoppingListObject = new JSONObject();
        shoppingList = new ArrayList<>();
    }

    /**
     * Writes shoppingListObject to a .json file using JSONWriter.
     * @param fileName the name of the .json file.
     * @return true if the write was successful.
     */
    public boolean writeToJSON(String fileName) {
        boolean writeSuccessful = false;
        shoppingListObject.addAttribute(new JSONAttribute("list", shoppingList));
        shoppingListObject.formatObject();

        try {
            jsonWriter = new JSONWriter(shoppingListObject, fileName);
            writeSuccessful = jsonWriter.writeFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return writeSuccessful;
    }
}
