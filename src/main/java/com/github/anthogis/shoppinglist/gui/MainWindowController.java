package com.github.anthogis.shoppinglist.gui;

import com.github.anthogis.shoppinglist.ParserInterface;
import com.github.anthogis.shoppinglist.ShoppingListItem;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.Optional;

public class MainWindowController {
    @FXML
    private TableView<ShoppingListItem> shoppingListTable;

    @FXML
    private MenuItem saveToJSON;

    @FXML
    private MenuItem saveToDropBox;

    @FXML
    private MenuItem saveToH2;

    @FXML
    private MenuItem closeMainWindow;

    @FXML
    private Button addToList;

    @FXML
    private TextField itemField;

    @FXML
    private TextField amountField;

    private ParserInterface parserInterface;

    public void initialize() {
        System.out.println("Initalizing " + getClass().toString());
        parserInterface = new ParserInterface();
    }

    public void saveToJSONAction() {
        System.out.println("json");
        if (parserInterface.writeToJSON()) {
            System.out.println("Write successful");
        } else {
            System.out.println("Write failed");
        }
    }

    public void saveToDropBoxAction() {
        System.out.println("droppis");
    }

    public void saveToH2Action() {
        System.out.println("hoo kakkone");
    }

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

    public void addToListAction() {
        String itemText = itemField.getText();
        String amountText = amountField.getText();

        if (validItemInput(itemText, amountText)) {
            itemField.setText("");
            amountField.setText("");
            int amount = Integer.parseInt(amountText);

            ObservableList<ShoppingListItem> shoppingListItems = shoppingListTable.getItems();
            shoppingListItems.add(new ShoppingListItem(itemText, amountText));
            parserInterface.addShoppingItem(itemText, amount);
        }
    }

    private boolean validItemInput(String itemText, String amountText) {
        boolean validItemInput = !itemText.equals("");

        try {
            Integer.parseInt(amountText);
        } catch (NumberFormatException e) {
            validItemInput = false;
        }

        return validItemInput;
    }
}
