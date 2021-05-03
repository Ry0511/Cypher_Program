package cyphergram.transposition.decrypt;

import cyphergram.transposition.utility.decrypt.CellStyle;
import cyphergram.transposition.utility.decrypt.GridPath;
import cyphergram.transposition.utility.decrypt.GridState;
import cyphergram.transposition.utility.encrypt.StringIterator;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import java.util.ArrayList;
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
     * more than one key.
     * <p>
     * Controlled/used: {@link #padKeyUpdated(KeyEvent)}
     */
    private static final String SINGLE_CHAR_ONLY = "One Char Only!";

    /**
     * Go button which once pressed will check if the grid should be updated,
     * or if an error message should be displayed.
     * <p>
     * Controlled: {@link #goButtonPressed()}
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
     * GridState object to allow ease of modifying and seeing the data inside
     * of {@link #gridBox} without dedicating hefty methods to this class.
     * <p>
     * Note this is instantiated at: {@link #goButtonPressed()}
     */
    private GridState gridState;

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
     * <p>
     * Controlled: {@link #padKeyUpdated(KeyEvent)}
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
     * String Iterator to keep track of the {@link #cypherText} text and
     * update labels later.
     * <p>
     * Declared in {@link #goButtonPressed()}
     * Used in {@link #gridLabelClicked(MouseEvent)}
     */
    private StringIterator cypherTextIterator;

    /**
     * GridPath Object to store the Node traversal for the Decryption process.
     * <p>
     * Declared in: {@link #goButtonPressed()}
     * Used in: {@link #gridLabelClicked(MouseEvent)}
     */
    private GridPath gridPath;

    /**
     * Makes a thread that will wait the provided time before enabling a
     * provided node.
     *
     * @param node     The node to enable after the wait time
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
     *
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
     * Creates the GridPane and populates it if the {@link #isStateReady()}
     * returns {@code true}. Else if {@code false} is returned then and Error
     * Alert message will be displayed.
     */
    public void goButtonPressed() {
        if (isStateReady()) {
            gridState = new GridState(gridBox);
            cypherTextIterator = new StringIterator(cypherText.getText());
            gridPath = new GridPath();
            handleGridCreation();
        } else {
            programStateNotReady();
        }
    }

    /**
     * Handle creating and populating the GridPane with the correct number of
     * Rows, Columns, and Cell Content.
     */
    private void handleGridCreation() {
        generateGrid(calculateGridSize());
        populateGrid();
    }

    /**
     * Calculates the GridPane Size by taking into account all input
     * parameters such as {@link #invertKeyBox} and {@link #gridKey}.
     *
     * @return Two positive integers (Zero included) in an Array referencing
     * Row, Col. (Index 0 == Rows).
     */
    private int[] calculateGridSize() {
        final int rows;
        final int columns;
        //Inverted: Row = Col.
        if (invertKeyBox.isSelected()) {
            columns = Integer.parseInt(gridKey.getText());

            //Avoid divide by zero
            if (columns > 0) {
                rows = cypherText.getText().length() / columns;
            } else {
                rows = cypherText.getText().length();
            }
        } else {
            //Normal: Row = Row.
            rows = Integer.parseInt(gridKey.getText());

            //Avoid divide by zero
            if (rows > 0) {
                columns = cypherText.getText().length() / rows;
            } else {
                columns = cypherText.getText().length();
            }
        }

        //NOTE ITS ALWAYS (ROW, COL); Just referenced values may be flipped.
        return new int[]{rows, columns};
    }

    /**
     * Creates the required number of Rows, Columns, Content, and Applies a
     * generic style to the Cells in the GridPane.
     *
     * @param gridSize The GridSize to create (Row, Col) Int Array.
     */
    private void generateGrid(final int[] gridSize) {
        final int rowIndex = 0;
        final int colIndex = 1;
        gridState.setRowCount(gridSize[rowIndex]);
        gridState.setColCount(gridSize[colIndex]);
        gridState.setCellPreferences(generateCellPreferences());
    }

    /**
     * @return A generic CellStyle for the Cells in the GridPane.
     */
    private CellStyle generateCellPreferences() {
        final CellStyle style = new CellStyle();
        final double fullSize = 100.0;
        //Default sizes
        style.setMinVal(0);
        style.setPrefVal(0);
        style.setMaxVal(Double.MAX_VALUE);

        //Sizing, Alignment
        style.setCellAlignment(CellStyle.Alignment.CENTER);
        style.setGrowPriority(Priority.ALWAYS);

        //Extra
        style.setFillState(false);
        style.setPercentSize(fullSize);
        return style;
    }

    /**
     * Populates the Grid with clickable labels, also does some backend stuff
     * such as clearing the old labels.
     */
    private void populateGrid() {
        cleanUpNodes(gridBox.getChildren());
        for (int rows = 0; rows < gridState.getRowCount(); rows++) {
            for (int cols = 0; cols < gridState.getColCount(); cols++) {
                Label e = new Label();
                e.setText(String.format("(%s, %s)", rows, cols));
                e.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
                e.autosize();
                e.setAlignment(Pos.CENTER);
                e.setId("buttonGrid");
                e.setOnMouseClicked(this::gridLabelClicked);
                gridState.addNode(e, rows, cols);
            }
        }
    }

    /**
     * Event Handler for when a GridLabel is clicked.
     *
     * @param event Event Trigger attached to the object which triggered the
     *              event.
     */
    private void gridLabelClicked(final MouseEvent event) {
        final int[] nodeIndex =
                gridState.getNodeIndex((Node) event.getSource());
        //Ensure event trigger node exists in the GridPane
        if (nodeIndex.length > 0) {
            Label e = (Label) gridState.getNodeAt(nodeIndex);
            updateGridLabel(e);
        }

        System.out.println("Current Path: " + gridPath.toString());
    }

    /**
     * Updates the provided GridLabel to whatever it needs to be, determined
     * upon by the state of the Grid.
     * <p>
     * Note this also does:
     * <p>
     * If the triggered {@link Label} is already in the {@code
     * Default-State} then it just updates the text to either use the
     * {@code Pad-Key} or directly use a value from
     * {@link #cypherText} which this value is determined via
     * {@link StringIterator#next()}. Then finally for the {@code
     * Label} the {@code CSS-ID} is changed to {@code
     * buttonGridClicked}. Then externally the {@link #gridPath} is
     * updated via {@link GridPath#addPath(int[])} using the Path
     * Index for the triggered Label.
     * </p>
     * <p>
     * But if the Label is not in the Default State then
     * {@link #resetLabelDefaults(Label)} is called on the Provided
     * label to put it back into the Default state, then the
     * {@link #gridPath} is updated, once the Grid Path is updated, all
     * labels not on the {@link #gridPath} are reset to their default states.
     * </p>
     *
     * @param label The label which was clicked and is the basis for which
     *              state the program should now be in.
     */
    private void updateGridLabel(final Label label) {
        if (isDefaultLabel(label)) {
            //Collect label text
            String text = "";
            if (cypherTextIterator.hasNext()) {
                text = cypherTextIterator.next();
            }
            text = text.replaceAll(" ", padKey.getText());

            //Update label
            label.setId("buttonGridClicked");
            label.setText(text);

            //Update GridPath
            gridPath.addPath(gridState.getNodeIndex(label));
        } else {
            //Set label text to default text
            resetLabelDefaults(label);

            //Update GridPath (Note a copy of the original path must be made
            // due to Concurrent Modification Issues)
            ArrayList<int[]> curFullPath =
                    new ArrayList<>(gridPath.getFullPath());
            this.gridPath.breakPathAt(gridState.getNodeIndex(label));
            curFullPath.removeAll(gridPath.getFullPath());
            fixDetachedLabels(curFullPath);

            //Repair String Iterator
            cypherTextIterator = repairCypherIterator();
        }
    }

    /**
     * Resets all Converts each Minor Path inside of the Provided full path
     * to Labels and then passes them through to
     * {@link #resetLabelDefaults(Label)} so that they can be reset.
     *
     * @param fullPath The path to follow and search for inside of
     * {@link #gridPath} which contains a 2D Array of {@link Node} Objects.
     */
    private void fixDetachedLabels(final ArrayList<int[]> fullPath) {
        ArrayList<Node> pathLabels =
                this.gridState.getCellContentFromPath(fullPath);

        for (Node e : pathLabels) {
            Label temp = (Label) e;
            resetLabelDefaults(temp);
        }
    }

    /**
     * Resets the provided GridLabel to its default text index value. "(Row,
     * Col)". Also re-assigns the CSS ID for this object.
     *
     * @param label The label to reset to default.
     */
    private void resetLabelDefaults(final Label label) {
        int[] defaultLabelVal = gridState.getNodeIndex(label);
        label.setText(String.format("(%s, %s)", defaultLabelVal[0],
                defaultLabelVal[1]));
        label.setId("buttonGrid");
    }


    /**
     * Iterates through the Current GridPath fixing the CypherText Iterator
     * to accommodate when a label is de-selected.
     *
     * @return String iterator, up-to the point in which the GridPath is broken.
     */
    private StringIterator repairCypherIterator() {
        StringIterator newIterator =
                new StringIterator(cypherTextIterator.toString());

        for (int[] minorPath : gridPath.getFullPath()) {
            Label e = (Label) gridState.getNodeAt(minorPath);
            if (!isDefaultLabel(e)) {
                newIterator.next();
            } else {
                return newIterator;
            }
        }
        return newIterator;
    }

    /**
     * Checks to see if the provided label has the Default text format of "
     * (x, y)" where x, y are integers/doubles.
     *
     * @param label The label to check for Default text on.
     *
     * @return {@code true} if the {@link Label#getText()} returns a String
     * matching {@code ^\([0-9]+.*[0-9]+\)$} -> "(x, y)". Else this method
     * will return {@code false}.
     */
    private boolean isDefaultLabel(final Label label) {
        //Pattern matches "(x, y)"
        final Pattern defaultRegex = Pattern.compile("^\\([0-9]+.*[0-9]+\\)$");
        return defaultRegex.matcher(label.getText()).matches();
    }

    /**
     * Removes all non Group Objects from the provided Observable list.
     *
     * @param children List of nodes to clean up.
     */
    private void cleanUpNodes(final ObservableList<Node> children) {
        children.removeIf(e -> e.getClass() != Group.class);
    }

    /**
     * Handles outputting error Alert messages in the case of the Program
     * state not being ready by providing and displaying targeted error
     * messages.
     */
    private void programStateNotReady() {
        //TODO
    }
}
