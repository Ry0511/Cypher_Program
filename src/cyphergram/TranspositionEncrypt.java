package cyphergram;

import cyphergram.transposition.utility.general.SceneUtils;
import cyphergram.transposition.utility.encrypt.Coordinates;
import cyphergram.transposition.utility.encrypt.GridPath;
import cyphergram.transposition.utility.encrypt.LabelProperties;
import cyphergram.transposition.utility.encrypt.StringIterator;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Controller class for the Transposition Cipher program. This code is messy
 * and needs a clean up but, this project itself was just a learning
 * experience so its not needed unless I want to.
 *
 * @author -Ry
 * @version 0.3
 * Copyright: N/A
 */
public class TranspositionEncrypt {
    private static final String GO_ERROR_TITLE = "Go! Failed!";
    private static final String GO_ERROR_HEADER = "Go failed to proceed.";
    private static final String GO_ERROR_MESSAGE = "One or more of the "
            + "provided parameters were invalid.%n%nCypher   : "
            + "%s%nPadding : %s%nRows      : %s%n";
    private static final String PATH_TITLE = "Full Path!";
    private static final String PATH_HEADER = "The full path currently is:"
            + " ((Row, Col) Indexes from Zero, Top left is 0, 0)";

    private final HashMap<Label, LabelProperties> labelStates = new HashMap<>();
    @FXML
    private Button decryptButton;
    @FXML
    private Label cypherTextLabel;
    @FXML
    private Label inputTextLabel;
    @FXML
    private Label previousCoordinateLabel;
    @FXML
    private Button fullPathButton;
    @FXML
    private GridPane gridBox;
    @FXML
    private TextField cypherText;
    @FXML
    private TextField padText;
    @FXML
    private TextField rowText;
    @FXML
    private Button goButton;
    private StringIterator cypher;
    private GridPath<Label> path;
    private ArrayList<Label> gridLabels;

    /**
     * Event listener attached to all Labels on the GridBox. Upon clicked this
     * will update the text on the label to what it should be for the
     * Transposition Cypher.
     *
     * @param event Event trigger, attached is the Object which triggered the
     *              event.
     */
    private void gridLabelClicked(MouseEvent event) {
        Label s = (Label) event.getSource();

        //HASN'T BEEN CLICKED
        if (!labelStates.get(s).getClickedState()) {
            //Case the label is a text label and hasn't been clicked
            if (cypher.hasNext()) {
                String text = cypher.next();
                if (text.equals(" ")) {
                    s.setText(padText.getText());
                } else {
                    s.setText(text);
                }

                //Case the label is a pad label and hasn't been clicked.
            } else {
                s.setText(padText.getText());
                labelStates.get(s).setPadLabel(true);
            }
            //Path must come before labels
            updatePath(s, true);

            //HAS BEEN CLICKED
        } else {
            //Case that label is a text label and has already been clicked
            if (cypher.hasPrevious()) {
                cypher.previous();
                s.setText(labelStates.get(s).getCoordinates());

                //Case label is a pad label and has already been clicked
            } else if (labelStates.get(s).isPadLabel()) {
                s.setText(labelStates.get(s).getCoordinates());
            }

            //Path must come before labels
            updatePath(s, false);
        }
        updateLabels();
        labelStates.get(s).setClickedState(!labelStates.get(s)
                .getClickedState());
    }

    /**
     * Makes a call to update the {@link #cypherTextLabel} and the
     * {@link #previousCoordinateLabel} to accommodate the program state change.
     *
     */
    private void updateLabels() {
        //Cypher, Previous Co-ords
        updateCypherLabel();
        if (path.getPath().size() > 1) {
            updatePrevCoordLabel();
        }
    }

    /**
     *
     */
    private void updateCypherLabel() {
        StringBuilder text = new StringBuilder();
        for (Label e : gridLabels) {
            String content = e.getText();
            if (content.length() > 1) {
                content = "";
            }
            text.append(content);
        }
        cypherTextLabel.setText(text.toString());
    }

    /**
     *
     */
    private void updatePrevCoordLabel() {
        Coordinates<Label> prev = path.getPath().get(path.length() - 1);
        previousCoordinateLabel.setText(prev.toString());
    }

    /**
     *
     */
    private void updatePath(Label e, boolean isAddState) {
        LabelProperties prop = labelStates.get(e);

        //Adding co-ordinates
        Coordinates<Label> tempCord = new Coordinates<>(prop.getCol(),
                prop.getRow());
        if (isAddState) {
            tempCord.setElement(e);
            path.addCoordinate(tempCord);

            //Removing co-ordinates
        } else {
            path.removeCoordinate(tempCord);
            fixStringIterator();
            updateBrokenLabels();
            resetLabels();
            updateCypherLabel();
            labelStates.get(e).setClickedState(
                    !labelStates.get(e).getClickedState());
        }
    }

    /**
     *
     */
    private void updateBrokenLabels() {
        Set<Label> labels = labelStates.keySet();

        for (Label label : labels) {
            int row = labelStates.get(label).getRow();
            int col = labelStates.get(label).getCol();
            Coordinates<Label> temp = new Coordinates<>(col, row);
            if (!path.contains(temp)) {
                label.setText(temp.toString());
                labelStates.get(label).setClickedState(false);
            }
        }
    }

    /**
     *
     */
    private void fixStringIterator() {
        cypher = new StringIterator(cypherText.getText());
        for (int i = 0; i < path.length(); i++) {
            if (cypher.hasNext()) {
                cypher.next();
            }
        }
    }

    /**
     * On button pressed this method will populate a grid with labels ready
     * to be pressed for a transposition cypher if the program state is ready
     * to do that.
     *
     */
    public void goButton() {
        //Case the program state is ready
        if (isGoReady()) {
            cypher = new StringIterator(cypherText.getText());
            path = new GridPath<>();
            gridLabels = new ArrayList<>();
            inputTextLabel.setText(cypherText.getText());
            int rows = Integer.parseInt(rowText.getText());
            int columns = cypherText.getText().length() / rows;

            if (rows * columns < cypherText.getText().length()) {
                columns++;
            }
            createGrid(rows, columns);
            populateGrid();
            resetLabels();
            //Case the program state isn't ready
        } else {
            String message = String.format(GO_ERROR_MESSAGE,
                    cypherText.getText(), padText.getText(),
                    rowText.getText());
            final String cssFile = "style.css";
            SceneUtils.alertUser(GO_ERROR_TITLE, GO_ERROR_HEADER, message,
                    this.getClass().getResource(cssFile));
        }
    }

    /**
     *
     */
    private void resetLabels() {
        cypherTextLabel.setText("");
        previousCoordinateLabel.setText("");
    }

    /**
     * Method that checks for a state in the program where, cypher text has
     * text, pad text has text, row text has/matches digits only and column
     * text matches digits only.
     *
     * @return {@code true} if the text areas are populated correctly as
     * expected. else {@code false}
     */
    private boolean isGoReady() {
        //Ensure generic text exists
        boolean a = cypherText.getText().length() > 0;
        boolean b = padText.getText().length() > 0;

        //Ensure digits only for row content (size is irrelevant since it
        // expects at least one digit.)
        boolean c =
                rowText.getText().matches("\\d+")
                        && !rowText.getText().equals("0");

        return a && b && c;
    }

    /**
     * Populates the GridBox with ((x, y) (rows, columns)).
     *
     * @param rows    The number of rows to create.
     * @param columns The number of columns to create.
     */
    private void createGrid(int rows, int columns) {
        gridBox.getColumnConstraints().clear();
        gridBox.getRowConstraints().clear();

        if (columns > 0) {
            //Columns
            for (int i = 0; i < columns; i++) {
                ColumnConstraints col = new ColumnConstraints();
                col.setFillWidth(true);
                col.setHgrow(Priority.ALWAYS);
                col.setHalignment(HPos.CENTER);
                gridBox.getColumnConstraints().add(col);
            }

            //Rows
            for (int j = 0; j < rows; j++) {
                RowConstraints row = new RowConstraints();
                row.setFillHeight(true);
                row.setVgrow(Priority.ALWAYS);
                row.setValignment(VPos.CENTER);
                gridBox.getRowConstraints().add(row);
            }
        }
    }

    /**
     * Populates all rows/columns in the grid with a label.
     */
    private void populateGrid() {
        final int rows = gridBox.getRowConstraints().size();
        final int cols = gridBox.getColumnConstraints().size();

        double width =
                gridBox.getColumnConstraints().get(0).getPrefWidth() / cols;
        double height =
                gridBox.getRowConstraints().get(0).getPrefHeight() / rows;
        clearGrid();
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Label temp = new Label();
                temp.setText(String.format("(%s, %s)", row, col));

                temp.setId("buttonGrid");
                temp.setAlignment(Pos.CENTER);
                temp.setOnMouseClicked(this::gridLabelClicked);

                temp.setPrefSize(width, height);
                temp.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                gridLabels.add(temp);
                labelStates.put(temp, new LabelProperties(temp.getText(),
                        false));
                gridBox.add(temp, col, row);
            }
        }
    }

    /**
     * Removes all labels from the GridBox Children List. (Concurrently)
     */
    private void clearGrid() {
        ObservableList<Node> temp = gridBox.getChildren();
        temp.removeIf(e -> e.getClass().equals(Label.class));
    }

    /**
     *
     */
    public void fullPathPressed() {
        final String textBase = "(%s, %s)";

        StringBuilder output = new StringBuilder();
        final int maxSize = path.getPath().size() -1;
        int count = 0;
        for (Coordinates<Label> cord : path.getPath()) {
            output.append(String.format(textBase, cord.getX(), cord.getY()));

            //Space each entry
            if (count != maxSize) {
                output.append(", ");
            }
            count++;
        }
        final String cssFile = "style.css";
        SceneUtils.alertUser(PATH_TITLE, PATH_HEADER, output.toString(),
                this.getClass().getResource(cssFile));
    }

    /**
     * Only attached to labels, method will copy the of the label to the
     * users OS Clipboard.
     */
    public void copyToClipboard(MouseEvent event) {
        Label temp = (Label) event.getSource();
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        content.putString(temp.getText());
        clipboard.setContent(content);
    }

    /**
     * Loads an Application Stage over the main stage that being the
     * Decryption stage.
     *
     * @throws IOException if the FXML file is missing or could not be parsed.
     */
    public void decryptBoard() throws IOException {
        //Stage data
        final String title = "Decrypt Transposition";
        final String fxml = "transposition/decrypt/decryptPage.fxml";
        final Parent root = FXMLLoader.load(getClass().getResource(fxml));

        //Stage setup
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }
}
