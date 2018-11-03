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

    public void initialize() {
        System.out.println("Initalizing " + getClass().toString());
    }
}
