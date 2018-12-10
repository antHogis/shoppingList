package com.github.anthogis.shoppinglist.gui;

/**
 * ActivityText contains messages that are intended for info prompts.
 *
 * <p>ActivityText contains short messages that are intended to be used on a label in MainWindow, which appears and disappears
 * quickly. These are meant to inform the user that an event occurred.</p>
 *
 * @author antHogis
 * @version 1.0
 * @since 1.0
 */
public enum ActivityText {
    WELCOME("Welcome!"),
    SAVE_SUCCESSFUL("Save successful!"),
    SAVE_FAILED("Save failed!"),
    SAVE_CANCELLED("Save cancelled!"),
    NOTHING_TO_SAVE("Nothing to save!"),
    ITEM_ADDED("Item added!"),
    INVALID_INPUT("Invalid input!"),
    LOGIN_SUCCESS("Log in successful!"),
    LOGIN_FAIL("Log in failed!"),
    NO_LOGIN("Please login to Dropbox."),
    FILE_FAIL("File couldn't be opened"),
    FILE_MALFORMED("File is malformed"),
    DBOX_AUTH_ERROR("Could not get link."),
    H2_LOGIN_ERROR("Could not access H2 database");


    String message;

    ActivityText(String message) {
        this.message = message;
    }
}
