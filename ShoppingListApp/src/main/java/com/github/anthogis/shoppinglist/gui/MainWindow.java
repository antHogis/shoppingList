package com.github.anthogis.shoppinglist.gui;


import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import javax.sound.midi.ControllerEventListener;
import java.io.IOException;

/**
 * The main window of the application's GUI.
 *
 * <p>The main window of the application's GUI. Utilizes an FXML file for defining the Nodes in the Stage of
 * the application. The FXML file defines the class <code>MainWindowController</code> to handle the events of MainWindow</p>
 *
 * @author antHogis
 * @version 1.3
 * @since 1.0
 */
public class MainWindow extends Application {
    private static final String WINDOW_TITLE = "Shopping List";
    private FXMLLoader loader;

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
        loader = new FXMLLoader(getClass().getResource("mainwindow.fxml"));
        Parent root = loader.load();

        stage.setMinHeight(600);
        stage.setMinWidth(800);
        stage.setTitle(WINDOW_TITLE);
        stage.initStyle(StageStyle.DECORATED);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("icon-shop.png")));

        stage.setScene(new Scene(root, 800, 600));
        stage.setResizable(false);
        //Platform.setImplicitExit(false);
        stage.setOnCloseRequest(this::closeEvent);
        stage.show();
    }

    /**
     * Method for handling closing of the window, implementation of EventHandler<WindowEvent>.
     *
     * @param event the event that caused request to close window.
     */
    private void closeEvent(WindowEvent event) {
        ((MainWindowController) loader.getController()).closeMainWindowAction();
        event.consume();
    }
}