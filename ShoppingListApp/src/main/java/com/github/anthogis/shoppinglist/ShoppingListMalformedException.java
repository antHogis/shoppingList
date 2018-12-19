package com.github.anthogis.shoppinglist;

/**
 * Exception thrown when a shopping list json cannot be parsed to a list of ShoppingListItems
 *
 * @author antHogis
 * @version 1.3
 * @since 1.2
 */
public class ShoppingListMalformedException extends RuntimeException {
    public ShoppingListMalformedException(String message) {
        super(message);
    }

    public ShoppingListMalformedException() {
        super("Something in the shoppinglist file was malformed.");
    }
}
