package CypherGram;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
/**
 * Default JavaFX main initialiser.
 *
 * @author -Ry
 * @version 1
 * Copyright: N/A
 */
public class Main extends Application {


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("transposition"
                + "/encrypt/transpositionEncrypt.fxml"));
        primaryStage.setTitle("CypherGram");
        primaryStage.setScene(new Scene(root, 1280, 768));
        primaryStage.show();
    }
}
