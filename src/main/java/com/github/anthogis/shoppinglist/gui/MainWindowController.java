package com.github.anthogis.shoppinglist.gui;

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

public class MainWindowController {
    @FXML
    private TableView<ShoppingListItem> shoppingListTable;

    @FXML
    private TextField itemField;

    @FXML
    private TextField amountField;

    @FXML
    private Label activityLabel;

    private FadeTransition fadeOutLabel;

    private ParserInterface parserInterface;

    public void initialize() {
        System.out.println("Initalizing " + getClass().toString());
        parserInterface = new ParserInterface();

        fadeOutLabel = new FadeTransition(Duration.millis(1500));
        fadeOutLabel.setNode(activityLabel);
        fadeOutLabel.setFromValue(0.0);
        fadeOutLabel.setToValue(1.0);
        fadeOutLabel.setCycleCount(2);
        fadeOutLabel.setAutoReverse(true);

        showMessage(ActivityText.WELCOME);
    }

    public void saveToJSONAction() {
        if (shoppingListTable.getItems().size() > 0) {
            for (ShoppingListItem item : shoppingListTable.getItems()) {
                parserInterface.addShoppingItem(item);
            }
            TextInputDialog fileNameInputDialog = new TextInputDialog("shopping-list");
            Optional<String> fileNameInput = fileNameInputDialog.showAndWait();

            if (fileNameInput.isPresent()) {
                fileNameInput.ifPresent(fileName -> {
                    if (parserInterface.writeToJSON(fileName)) {
                        showMessage(ActivityText.SAVE_SUCCESSFUL);
                    } else {
                        showMessage(ActivityText.SAVE_FAILED);
                    }
                });
            } else {
                showMessage(ActivityText.SAVE_CANCELLED);
            }
            parserInterface.clearShoppingList();

        } else {
            showMessage(ActivityText.NOTHING_TO_SAVE);
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

    public void clearTableAction() {
        shoppingListTable.getItems().clear();
        parserInterface.clearShoppingList();
    }

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

    private boolean validItemInput(String itemText, String amountText) {
        boolean validItemInput = !itemText.equals("");

        try {
            Integer.parseInt(amountText);
        } catch (NumberFormatException e) {
            validItemInput = false;
        }

        return validItemInput;
    }

    private void showMessage(ActivityText message) {
        activityLabel.setText(message.message);
        activityLabel.setVisible(true);
        fadeOutLabel.playFromStart();
    }
}
