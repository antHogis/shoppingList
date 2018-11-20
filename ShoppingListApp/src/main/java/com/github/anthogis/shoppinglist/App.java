package com.github.anthogis.shoppinglist;

import com.github.anthogis.shoppinglist.gui.MainWindow;
import javafx.application.Application;

/**
 * The main class of the application.
 *
 * <p>The main class of the application. Launches MainWindow.</p>
 *
 * @author antHogis
 * @version 1.0
 * @since 1.0
 */
public class App {

    /**
     * Launches MainWindow.
     *
     * @param args command line parameters, passed to javafx.application.Application.launch().
     */
    public static void main( String[] args ) {
        System.out.printf("Author: %s\n", "Anton Höglund");
        Application.launch(MainWindow.class, args);
    }
}