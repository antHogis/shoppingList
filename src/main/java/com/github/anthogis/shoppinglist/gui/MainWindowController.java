package com.github.anthogis.shoppinglist.gui;

import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;

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
        
    }
}
