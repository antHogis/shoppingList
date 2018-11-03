package com.github.anthogis.shoppinglist.gui;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainWindow extends Application {
    private static final String WINDOW_TITLE = "Shopping List";

    @Override
    public void start(Stage stage) {
        BorderPane bPane = new BorderPane();
        stage.setTitle(WINDOW_TITLE);
        stage.initStyle(StageStyle.DECORATED);

        stage.setScene(new Scene(bPane, 640, 480));
        stage.setResizable(false);
        stage.show();

    }
}