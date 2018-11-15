package com.github.anthogis.shoppinglist;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ShoppingListItem {
    private final SimpleStringProperty itemName = new SimpleStringProperty("");
    private final SimpleStringProperty itemAmount = new SimpleStringProperty("");

    public ShoppingListItem(String itemName, String itemAmount) {
        setItemName(itemName);
        setItemAmount(itemAmount);
    }

    public ShoppingListItem() {
        this("","");
    }

    public String getItemName() {
        return itemName.get();
    }

    public void setItemName(String itemName) {
        this.itemName.set(itemName);
    }

    public String getItemAmount() {
        return itemAmount.get();
    }

    public void setItemAmount(String itemAmount) {
        this.itemAmount.set(itemAmount);
    }
}
