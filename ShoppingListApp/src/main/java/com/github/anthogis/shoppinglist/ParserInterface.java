package com.github.anthogis.shoppinglist;

import com.github.anthogis.json_parser.api.JSONAttribute;
import com.github.anthogis.json_parser.api.JSONObject;
import com.github.anthogis.json_parser.api.JSONWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * ParserInterface establishes connectivity between <code>MainWindowController</code> and the JSON parser.
 *
 * <p>ParserInterface establishes connectivity between <code>MainWindowController</code> and the JSON parser.
 * ParserInterface holds a list of <code>JSONObject</code>s of <code>ShoppingListItem</code>s in shoppingList, which
 * is added as an attribute to <code>JSONObject</code> shoppingListObject.</p>
 *
 * <p>This interface writes the contents of <code>shoppingListObject</code> to a .json file using a
 * <code>JSONWriter</code>.</p>
 *
 * @author antHogis
 * @version 1.3
 * @since 1.0
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

    /**
     * The list of JSONObjects of ShoppingListItems.
     */
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
        itemObject.add(new JSONAttribute("productName", item.getItemName()));
        itemObject.add(new JSONAttribute("productAmount", itemAmount));
        shoppingList.add(itemObject);
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
     * @param addSuffix whether ".json" should be appended to the end of the filename or not.
     * @return true if the write was successful.
     */
    public boolean writeToJSON(String fileName, boolean addSuffix) {
        boolean writeSuccessful = false;
        shoppingListObject.add(new JSONAttribute("list", shoppingList));

        try {
            jsonWriter = new JSONWriter(shoppingListObject, fileName, addSuffix);
            writeSuccessful = jsonWriter.writeFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return writeSuccessful;
    }
}
