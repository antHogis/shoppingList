package com.github.anthogis.shoppinglist;

import javafx.beans.property.SimpleStringProperty;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Template for an item in a shopping list.
 *
 * <p>Template for an item in a shopping list. Holds information of an item's name, and the amount of an item. Used in
 * conjunction with a TableView.</p>
 *
 * @author antHogis
 * @version 1.0
 * @since 1.0
 */
public class ShoppingListItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    /**
     * The name of the item.
     */
    @Column(name = "itemName")
    private final SimpleStringProperty itemName = new SimpleStringProperty("");

    /**
     * The amount of the item.
     */
    @Column(name = "itemAmount")
    private final SimpleStringProperty itemAmount = new SimpleStringProperty("");

    /**
     * The constructor for ShoppingListItem.
     *
     * <p>The constructor for ShoppingListItem. Assigns the argument values to the attributes of the ShoppingListItem.</p>
     * @param itemName
     * @param itemAmount
     */
    public ShoppingListItem(String itemName, String itemAmount) {
        setItemName(itemName);
        setItemAmount(itemAmount);
    }

    /**
     * The constructor for ShoppingListItem.
     *      *
     *      * <p>The constructor for ShoppingListItem. Assigns empty Strings to the attributes of the ShoppingListItem.</p>
     */
    public ShoppingListItem() {
        this("","");
    }


    /**
     * Returns the name of the item.
     * @return the name of the item.
     */
    public String getItemName() {
        return itemName.get();
    }

    /**
     * Sets the name of the item.
     * @param itemName the name to set to the item.
     */
    public void setItemName(String itemName) {
        this.itemName.set(itemName);
    }

    /**
     * Returns the amount of the item.
     * @return the amount of the item.
     */
    public String getItemAmount() {
        return itemAmount.get();
    }

    /**
     * Sets the amount of the item.
     * @param itemAmount the amount of the item to set.
     */
    public void setItemAmount(String itemAmount) {
        this.itemAmount.set(itemAmount);
    }
}
