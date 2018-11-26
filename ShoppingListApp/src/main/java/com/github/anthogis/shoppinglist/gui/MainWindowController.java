package com.github.anthogis.shoppinglist.gui;

import com.dropbox.core.DbxException;
import com.github.anthogis.shoppinglist.DBoxInterface;
import com.github.anthogis.shoppinglist.ParserInterface;
import com.github.anthogis.shoppinglist.ShoppingListItem;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.util.Duration;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * Event handler for <code>MainWindow</code>.
 *
 * <p>MainWindowController handles events of MainWindow. This class is notated as the controller for <code>MainWindow</code>
 * in <code>mainwindow.fxml</code>. Public methods within this class are notated as "onAction"
 * methods for Nodes in the <code>mainwindow.fxml</code> document, except for <code>void initialize()</code>.</p>
 * <p>Events that this class handles consists of adding items to the shopping list, calling a <code>ParserInterface</code>
 * to save the shopping list, and informing the user of events occurring.</p>
 *
 * @author antHogis
 * @version 1.0
 * @since 1.0
 */
public class MainWindowController {

    /**
     * The table which displays the shopping list. Reference to FXML Node defined in <code>mainwindow.fxml</code>.
     */
    @FXML
    private TableView<ShoppingListItem> shoppingListTable;

    /**
     * The text field for giving input for the name of an item for the shopping list. Reference to FXML Node defined in <code>mainwindow.fxml</code>.
     */
    @FXML
    private TextField itemField;

    /**
     * The text field for giving input for the amount of an item for the shopping list. Reference to FXML Node defined in <code>mainwindow.fxml</code>.
     */
    @FXML
    private TextField amountField;

    /**
     * Informs the user messages of activities, like confirmation for successful saving of a file. Reference to FXML Node defined in <code>mainwindow.fxml</code>.
     */
    @FXML
    private Label activityLabel;

    /**
     * The transition which fades <code>activityLabel</code> in and out.
     */
    private FadeTransition fadeOutLabel;

    /**
     * The interface between this class and a JSONWriter
     */
    private ParserInterface parserInterface;

    /**
     * The interface to Dropbox.
     */
    private DBoxInterface dBoxInterface;

    /**
     * Lifecycle method. Called after @FXML annotated fields are populated.
     *
     * <p>Lifecycle method. Called after @FXML annotated fields are populated. Initializes <code>parserInterface</code>
     * and the <code>fadeOutLabel</code> transition.</p>
     */
    public void initialize() {
        System.out.println("Initalizing " + getClass().toString());
        parserInterface = new ParserInterface();
        dBoxInterface = new DBoxInterface();

        fadeOutLabel = new FadeTransition(Duration.millis(1500));
        fadeOutLabel.setNode(activityLabel);
        fadeOutLabel.setFromValue(0.0);
        fadeOutLabel.setToValue(1.0);
        fadeOutLabel.setCycleCount(2);
        fadeOutLabel.setAutoReverse(true);

        showMessage(ActivityText.WELCOME);
    }

    /**
     * Event called when <code>MenuItem saveToJSON</code> is clicked.
     *
     * <p>Event called when <code>MenuItem saveToJSON</code> is clicked. Uses method {@link #saveJson(Consumer)}
     * to save the shopping list to json locally with <code>parserInterface</code>.
     */
    public void saveToJSONAction() {
        saveJson(fileName -> {
            if (parserInterface.writeToJSON(fileName, true)) {
                showMessage(ActivityText.SAVE_SUCCESSFUL);
            } else {
                showMessage(ActivityText.SAVE_FAILED);
            }
        });
    }

    /**
     * <p>Event called when <code>MenuItem saveToJSON</code> is clicked. Uses method {@link #saveJson(Consumer)}
     * to save the shopping list to json to DropBox by calling {@link com.github.anthogis.shoppinglist.DBoxInterface#saveAndUpload(String, ParserInterface)}.
     */
    public void saveToDropBoxAction() {
        saveJson(fileName -> {
            try {
                if (dBoxInterface.saveAndUpload(fileName, parserInterface)) {
                    showMessage(ActivityText.SAVE_SUCCESSFUL);
                } else {
                    showMessage(ActivityText.SAVE_FAILED);
                }
            } catch (DBoxInterface.DBoxBadLoginException e) {
                showMessage(ActivityText.NO_LOGIN);
            }

        });
    }

    /**
     * Event called when <code>MenuItem saveToH2</code> is clicked.
     */
    public void saveToH2Action() {
        System.out.println("hoo kakkone");
    }

    /**
     * Event called when <code>MenuItem closeMainWindow</code> is clicked.
     *
     * <p>Event called when <code>MenuItem closeMainWindow</code> is clicked. Displays a confirmation alert to ensure
     * that the player wants to close the application.</p>
     */
    public void closeMainWindowAction() {
        Alert closeAlert = new Alert(Alert.AlertType.CONFIRMATION);
        closeAlert.setTitle("Warning!");
        closeAlert.setHeaderText("You are discarding unsaved changes");
        closeAlert.setContentText("If you close now, all unsaved changed will be discarded.\n" +
                "Close anyway?");

        closeAlert.getButtonTypes().setAll(ButtonType.CLOSE, ButtonType.CANCEL);

        Optional<ButtonType> result = closeAlert.showAndWait();
        if (result.get() == ButtonType.CLOSE) {
            Platform.exit();
        } else {
            closeAlert.close();
        }
    }

    public void logInToDropBoxAction() {
        TextInputDialog accessTokenInputDialog = new TextInputDialog(null);
        Optional<String> accessTokenInput = accessTokenInputDialog.showAndWait();

        if (accessTokenInput.isPresent()) {
            accessTokenInput.ifPresent(accessToken -> {
                try {
                    dBoxInterface.login(accessToken);
                    showMessage(ActivityText.LOGIN_SUCCESS);
                } catch (DbxException e) {
                    showMessage(ActivityText.LOGIN_FAIL);
                }

            });
        }
    }

    /**
     * Event called when <code>MenuItem clearTable</code> is clicked.
     *
     * <p>Event called when <code>MenuItem clearTable</code> is clicked. Deletes all items in the
     * <code>shoppingListTable</code> and <code>parserInterface</code>.</p>
     */
    public void clearTableAction() {
        shoppingListTable.getItems().clear();
        parserInterface.clearShoppingList();
    }

    /**
     * Event called when <code>Button addToList</code> is clicked.
     *
     * <p>Event called when <code>Button addToList</code> is clicked. Adds user input to shopping list if it is valid.</p>
     */
    public void addToListAction() {
        String itemText = itemField.getText();
        String amountText = amountField.getText();

        if (validItemInput(itemText, amountText)) {
            showMessage(ActivityText.ITEM_ADDED);
            itemField.setText("");
            amountField.setText("");

            ShoppingListItem shoppingListItem = new ShoppingListItem(itemText, amountText);
            shoppingListTable.getItems().add(shoppingListItem);
        } else {
            showMessage(ActivityText.INVALID_INPUT);
        }
    }

    /**
     * Helper method for saving JSON files.
     */
    private void saveJson(Consumer<String> consumer) {
        if (shoppingListTable.getItems().size() > 0) {
            parserInterface.clearShoppingList();
            for (ShoppingListItem item : shoppingListTable.getItems()) {
                parserInterface.addShoppingItem(item);
            }
            TextInputDialog fileNameInputDialog = new TextInputDialog("shopping-list");
            Optional<String> fileNameInput = fileNameInputDialog.showAndWait();

            if (fileNameInput.isPresent()) {
                fileNameInput.ifPresent(consumer);
            } else {
                showMessage(ActivityText.SAVE_CANCELLED);
            }
        } else {
            showMessage(ActivityText.NOTHING_TO_SAVE);
        }
    }

    /**
     * Verifies that one String is not empty, and that that the other can be parsed to an integer.
     *
     * @param itemText the String that should not be empty.
     * @param amountText the String that should be parsable to an integer.
     * @return true if the conditions are met.
     */
    private boolean validItemInput(String itemText, String amountText) {
        boolean validItemInput = !itemText.equals("");

        try {
            Integer.parseInt(amountText);
        } catch (NumberFormatException e) {
            validItemInput = false;
        }

        return validItemInput;
    }

    /**
     * Manipulates <code>activityLabel</code> to show message.
     *
     * <p>Changes the text of <code>activityLabel</code> to a pre-defined message from an <code>ActivityText</code>.</p>
     * Starts a transition on <code>activityLabel</code> through <code>fadeOutLabel</code>.
     *
     * @param message the pre-defined message.
     */
    private void showMessage(ActivityText message) {
        activityLabel.setText(message.message);
        activityLabel.setVisible(true);
        fadeOutLabel.playFromStart();
    }

}