package com.github.anthogis.shoppinglist.gui;

public enum ActivityText {
    WELCOME("Welcome!"),
    SAVE_SUCCESSFUL("Save successful!"),
    SAVE_FAILED("Save failed!"),
    SAVE_CANCELLED("Save cancelled!"),
    ITEM_ADDED("Item added!"),
    INVALID_INPUT("Invalid input!");

    String message;

    private ActivityText(String message) {
        this.message = message;
    }
}