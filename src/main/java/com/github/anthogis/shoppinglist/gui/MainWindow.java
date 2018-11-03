package com.github.anthogis.shoppinglist.gui;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class MainWindow extends Application {
    private static final String WINDOW_TITLE = "Shopping List";

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("mainwindow.fxml"));

        stage.setTitle(WINDOW_TITLE);
        stage.initStyle(StageStyle.DECORATED);

        stage.setScene(new Scene(root, 640, 480));
        stage.setResizable(false);
        stage.show();

    }
}