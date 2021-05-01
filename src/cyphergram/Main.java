package cyphergram;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
/**
 * Default JavaFX main initialiser.
 *
 * @author -Ry
 * @version 1.1
 * Copyright: N/A
 */
public class Main extends Application {


    /**
     * Main method just runs {@link #launch(String...)}.
     */
    public static void main(final String[] args) {
        launch(args);
    }

    /**
     * Start the GUI program.
     *
     * @param primaryStage The default stage produced from
     * {@link #launch(String...)}
     */
    @Override
    public void start(final Stage primaryStage) throws Exception {
        final String fxmlFile = "transposition/encrypt/transpositionEncrypt"
                + ".fxml";
        Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
        primaryStage.setTitle("cyphergram");

        final int defaultWidth = 1280;
        final int defaultHeight = 768;
        primaryStage.setScene(new Scene(root, defaultWidth, defaultHeight));
        primaryStage.show();
    }
}
