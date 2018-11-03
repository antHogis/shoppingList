package com.github.anthogis.shoppinglist.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;

public class MainWindowController {
    @FXML
    private TableView shoppingListTable;

    @FXML
    private Button saveToJSON;

    @FXML
    private Button saveToDropBox;

    @FXML
    private Button saveToH2;
}
