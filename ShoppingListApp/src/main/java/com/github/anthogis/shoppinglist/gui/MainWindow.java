package com.github.anthogis.shoppinglist.gui;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

/**
 * The main window of the application's GUI.
 *
 * <p>The main window of the application's GUI. Utilizes an FXML file for defining the Nodes in the Stage of
 * the application. The FXML file defines the class <code>MainWindowController</code> to handle the events of MainWindow</p>
 */
public class MainWindow extends Application {
    private static final String WINDOW_TITLE = "Shopping List";

    /**
     * Life cycle method, called when the application starts.
     *
     * <p>Life cycle method, called when the application starts. Loads the FXML file, initializes basic attributes of stage.</p>
     *
     * @param stage the Stage for the application
     * @throws IOException if the FXML file cannot be loaded
     */
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("mainwindow.fxml"));

        stage.setMinHeight(600);
        stage.setMinWidth(800);
        stage.setTitle(WINDOW_TITLE);
        stage.initStyle(StageStyle.DECORATED);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("icon-shop.png")));
        stage.setScene(new Scene(root, 640, 480));
        stage.setResizable(false);
        stage.show();
    }
}