package com.github.anthogis.shoppinglist.gui;

import com.sun.org.apache.xerces.internal.xs.datatypes.ByteList;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;

import java.util.Optional;

public class MainWindowController {
    @FXML
    private TableView shoppingListTable;

    @FXML
    private MenuItem saveToJSON;

    @FXML
    private MenuItem saveToDropBox;

    @FXML
    private MenuItem saveToH2;

    @FXML
    private MenuItem closeMainWindow;

    public void initialize() {
        System.out.println("Initalizing " + getClass().toString());
    }

    public void saveToJSONAction() {
        System.out.println("json");
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
}
