package cyphergram.transposition.utility.general;

import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;

import java.net.URL;

/**
 * Utility class designed to deal with displaying and handling scene updates
 * such as Alert Messages.
 */
public final class SceneUtils {

    /**
     * Hide constructor for utility class.
     */
    private SceneUtils() {

    }
    /**
     * Displays an information error message to the screen using the provided
     * Strings for the content to be displayed.
     *
     * @param title   Title of the Alert Container.
     * @param header  Header text for the Alert Container.
     * @param message Message/content to be displayed.
     * @param cssFile The Style file to use for the created Alert window.
     */
    public static void alertUser(final String title, final String header,
                                 final String message, final URL cssFile) {
        Alert e = new Alert(Alert.AlertType.INFORMATION);
        e.setTitle(title);
        e.setHeaderText(header);
        e.setContentText(message);

        DialogPane dialogPane = e.getDialogPane();
        dialogPane.getStylesheets().add(cssFile.toExternalForm());
        dialogPane.getStyleClass().add("myDialog");

        e.setResizable(true);
        e.showAndWait();
    }
}
