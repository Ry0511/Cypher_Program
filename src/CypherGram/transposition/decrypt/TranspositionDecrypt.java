package CypherGram.transposition.decrypt;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;

import java.util.regex.Pattern;

/**
 * Controller for the Decryption side of the Transposition Cypher programme.
 * This controller is abstract from the main Transposition Cypher controller,
 * but still retains some recent data from that controller such as the
 * Previous Path, and Previous Pad Key.
 *
 * @author -Ry
 * @version 0.1
 * Copyright: N/A
 */
public class TranspositionDecrypt {

    /**
     * Prompt text for {@link #gridKey}, only set after the user attempts to
     * input something that isn't a digit. Controlled/used:
     * {@link #keyTextUpdated(KeyEvent)}
     */
    private static final String KEY_PROMPT_TEXT = "[0-9] Only!";
    /**
     * Prompt text for {@link #padKey}, only set after user attempts to input
     * more than one key. Controlled/used: {@link #padKeyUpdated(KeyEvent)}
     */
    private static final String SINGLE_CHAR_ONLY = "One Char Only!";

    /**
     * Go button which once pressed will check if the grid should be updated,
     * or if an error message should be displayed.
     */
    @FXML
    private Button goButton;

    /**
     * GridBox to be populated with labels, initialised to a default state of
     * 2x3 and fixed inside of an Anchor Pane to allow auto re-sizing of the
     * grid as the Main Window Size changes.
     * <p>
     * {@link #gridKey} Defines the Grid size later on.
     */
    @FXML
    private GridPane gridBox;

    /**
     * Label which shows what the Decrypted text is using the path defined in
     * the program. Has placeholder text of "?"
     */
    @FXML
    private Label decryptedTextLabel;

    /**
     * Checkbox which determines if the key should be inverted or not. This
     * is defaulted to {@code false} (Un-checked), when checked the
     * {@link #gridKey} will stand for minimum number of Columns rather than
     * minimum number of Rows.
     */
    @FXML
    private CheckBox invertKeyBox;

    /**
     * Text Field which defines what the Pad Key is for the encrypted text.
     */
    @FXML
    private TextField padKey;

    /**
     * Input text field which expects "[0-9]+" as input. As the
     * text changes the {@link #gridBox} will change size to accommodate the
     * key and the {@link #cypherText} (Grid size must be greater than or
     * equal to the number of characters in the text)
     * <br><br>
     * Note: {@link #invertKeyBox} Defines Row/Col for the key.
     */
    @FXML
    private TextField gridKey;

    /**
     * Cypher text field which contains the current text to decrypt.
     */
    @FXML
    private TextField cypherText;

    /**
     * Makes a thread that will wait the provided time before enabling a
     * provided node.
     *
     * @param node The node to enable after the wait time
     * @param duration The time to wait for in Milliseconds
     */
    private static void enableNodeAfter(final Node node, final long duration) {
        Runnable e = () -> {
            try {
                Thread.sleep(duration);
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
            node.setDisable(false);
        };
        Thread temp = new Thread(e);
        temp.start();
    }

    /**
     * @return {@code true} if the Cypher Text and Grid Text are present.
     * else return {@code false}.
     */
    public boolean isStateReady() {
        boolean cypherReady = cypherText.getText().length() > 0;
        boolean keyReady = gridKey.getText().length() > 0;
        boolean padReady = padKey.getText().length() > 0;

        return cypherReady && keyReady && padReady;
    }

    /**
     * Event handler attached to {@link #gridKey} for when the text is
     * updated. When the text is updated it enforces the following:<br><br>
     * <ul>
     *     <li>Text must match [0-9]+ (Numbers only)</li>
     *     <li>If the text doesn't it clears the Text Field, disables it,
     *     and then re-enables it after a set period of time.</li>
     * </ul>
     * @param event KeyEvent which contains the key that triggered the event.
     */
    @FXML
    private void keyTextUpdated(final KeyEvent event) {
        //Ensure key is [0-9+] only
        final String digitRegex = "\\d+";
        final long waitTime = 1500;
        if (!event.getCharacter().matches(digitRegex)) {
            gridKey.setDisable(true);
            gridKey.clear();
            gridKey.setPromptText(KEY_PROMPT_TEXT);
            enableNodeAfter(gridKey, waitTime);
        }
    }

    /**
     * Handles updates on {@link #padKey} to ensure that the text remains in
     * a valid range. If it ever goes out of range; the node is disabled and
     * then {@link #enableNodeAfter(Node, long)} is called to enable after a
     * set period.
     *
     * @param event Key event which contains the key that was pressed.
     */
    @FXML
    private void padKeyUpdated(final KeyEvent event) {
        //State checks
        final long waitTime = 1500;
        final Pattern charRegex = Pattern.compile(".+$");
        final boolean isInputText = charRegex.matcher(
                event.getText()).matches();
        //Ensure valid input range
        if (padKey.getText().length() > 0 && !isInputText) {
            padKey.setDisable(true);
            padKey.clear();
            padKey.setPromptText(SINGLE_CHAR_ONLY);
            enableNodeAfter(padKey, waitTime);
        }
    }

    /**
     *
     */
    public void goButtonPressed() {
        if (isStateReady()) {
            //TODO Get/Set Grid State, Create Grid, Populate Grid.
        } else {
            //TODO Error output/log
        }
    }
}
