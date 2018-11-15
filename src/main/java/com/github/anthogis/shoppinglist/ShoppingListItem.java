package com.github.anthogis.shoppinglist;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ShoppingListItem {
    private final SimpleStringProperty itemName = new SimpleStringProperty("");
    private final SimpleIntegerProperty itemAmount = new SimpleIntegerProperty(0);

    public ShoppingListItem(String itemName, int itemAmount) {
        setItemName(itemName);
        setItemAmount(itemAmount);
    }

    public ShoppingListItem() {
        this("",0);
    }

    public String getItemName() {
        return itemName.get();
    }

    public void setItemName(String itemName) {
        this.itemName.set(itemName);
    }

    public int getItemAmount() {
        return itemAmount.get();
    }

    public void setItemAmount(int itemAmount) {
        this.itemAmount.set(itemAmount);
    }
}
