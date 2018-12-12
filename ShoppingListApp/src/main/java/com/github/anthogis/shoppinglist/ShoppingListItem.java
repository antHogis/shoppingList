package com.github.anthogis.shoppinglist;

import javafx.beans.property.SimpleStringProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Template for an item in a shopping list.
 *
 * <p>Template for an item in a shopping list. Holds information of an item's name, and the amount of an item. Used in
 * conjunction with a TableView.</p>
 *
 * @author antHogis
 * @version 1.3
 * @since 1.0
 */
@Entity
@Table(name = "ShoppingList")
public class ShoppingListItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "itemName")
    private String itemName;

    @Column(name = "itemAmount")
    private int itemAmount = 0;

    /**
     * The name of the item.
     */
    @Transient
    private final SimpleStringProperty itemNameProperty = new SimpleStringProperty("");

    /**
     * The amount of the item.
     */
    @Transient
    private final SimpleStringProperty itemAmountProperty = new SimpleStringProperty("");

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
        this("","0");
    }


    /**
     * Returns the name of the item.
     * @return the name of the item.
     */
    public String getItemName() {
        return itemNameProperty.get();
    }

    /**
     * Sets the name of the item.
     * @param itemName the name to set to the item.
     */
    public void setItemName(String itemName) {
        this.itemName = itemName;
        this.itemNameProperty.set(itemName);
    }

    /**
     * Returns the amount of the item.
     * @return the amount of the item.
     */
    public String getItemAmount() {
        return itemAmountProperty.get();
    }

    /**
     * Sets the amount of the item.
     * @param itemAmount the amount of the item to set.
     */
    public void setItemAmount(String itemAmount) {
        this.itemAmount = Integer.parseInt(itemAmount);
        this.itemAmountProperty.set(itemAmount);
    }
}
