package com.github.anthogis.shoppinglist;

public class ShoppingListMalformedException extends RuntimeException {
    public ShoppingListMalformedException(String message) {
        super(message);
    }

    public ShoppingListMalformedException() {
        super("Something in the shoppinglist file was malformed.");
    }
}
