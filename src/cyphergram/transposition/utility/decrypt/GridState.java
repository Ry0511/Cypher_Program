package cyphergram.transposition.utility.decrypt;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.util.ArrayList;

/**
 * Takes in a GridPane and assigns states to this object which relate to the
 * GridPane such as the number of Rows, Columns, Content and so on.
 *
 * @author -Ry
 * @version 0.1
 * Copyright: N/A
 */
public class GridState {
    /**
     * The GridPane to modify and or see the status of in simple method calls.
     */
    private final GridPane gridPane;

    /**
     * The elements held in the GridPane...
     * <p>
     * Note The Following:
     * <ul>
     *     <li>This assumes that the GridPane is empty upon creation.</li>
     *     <li>When adding Columns or Rows this is cleared and reset so changing
     *     the GridPane via {@link #setColCount(int)} or
     *     {@link #setRowCount(int)} will empty this Array.
     *     </li>
     *     <li>Elements are only added to this Array
     *     through {@link #addNode(Label, int, int)}</li>
     * </ul>
     */
    private Node[][] gridCellContent;

    /**
     * Construct our GridState from a provided GridPane.
     *
     * @param grid The GridPane base for this instance.
     */
    public GridState(final GridPane grid) {
        this.gridPane = grid;
        updateCellContent();
    }

    /**
     * @return The current number of Columns on the {@link #gridPane}.
     */
    public int getColCount() {
        return gridPane.getColumnConstraints().size();
    }

    /**
     * Set the number of Columns for {@link #gridPane}. Does this by clearing
     * the columns and then adding new Columns iteratively upto the provided
     * integer.
     *
     * @param columns The number of columns to display/create for
     *                {@link #gridPane}.
     */
    public void setColCount(final int columns) {
        gridPane.getColumnConstraints().clear();
        for (int i = 0; i < columns; i++) {
            gridPane.getColumnConstraints().add(new ColumnConstraints());
        }
        updateCellContent();
    }

    /**
     * @return The current number of Rows on the {@link #gridPane}.
     */
    public int getRowCount() {
        return gridPane.getRowConstraints().size();
    }

    /**
     * Set the number of Rows for this GridPane to display. Does this by
     * clearing the current number of Rows and then adding new Rows.
     * <p>
     * Note: Only edits and modifies the Number of Rows, Child nodes and such
     * are not affected.
     *
     * @param rows The number of Rows to display on {@link #gridPane}.
     */
    public void setRowCount(final int rows) {
        gridPane.getRowConstraints().clear();
        for (int i = 0; i < rows; i++) {
            gridPane.getRowConstraints().add(new RowConstraints());
        }
        updateCellContent();
    }

    /**
     * Creates a new 2D Node Array to store the GridPanes Node content
     * positions.
     * <p>
     * Note this assumes that the GridPane is empty and has no child nodes.
     */
    private void updateCellContent() {
        this.gridCellContent = new Node[this.getRowCount()][this.getColCount()];
    }

    /**
     * Applies the Provided CellStyle to the currently stored GridPane.
     *
     * @param cellStyle The Style to apply to the GridPane.
     */
    public void setCellPreferences(final CellStyle cellStyle) {
        cellStyle.applyStyleToGrid(this.gridPane);
    }

    /**
     * Places and the provided node in the GridPane at the row, col position
     * provided. Also makes a note of this to allow for ease of access in the
     * GridPane's stored node elements.
     * <p>
     * Note if there is already a node in the position being added then the
     * existing node will be deleted/replaced from the GridPane.
     *
     * @param node Node to add to the GridPane.
     * @param row  The Row to put the Node into.
     * @param col  The Column to put the node into.
     */
    public void addNode(final Label node, final int row, final int col) {
        if (this.getColCount() > col && this.getRowCount() > row) {
            //If node exists, delete the node from the GridPane
            Node temp = gridCellContent[row][col];
            if (temp != null) {
                gridPane.getChildren().removeIf(i -> i.equals(temp));
            }

            //Add node, and update Grid Cell
            gridPane.add(node, col, row);
            gridCellContent[row][col] = node;
        }
    }

    /**
     * @param node The node to look for and get the index of.
     * @return Int array of (Row, Col) indicating the position of the Node by
     * its cartesian co-ordinates. If the Node could not be found then the
     * returned Array is of size Zero.
     */
    public int[] getNodeIndex(final Node node) {
        for (int row = 0; row < getRowCount(); row++) {
            for (int col = 0; col < getColCount(); col++) {
                if (gridCellContent[row][col] == node) {
                    return new int[]{row, col};
                }
            }
        }
        //int array of size zero indicates element does not exist.
        return new int[0];
    }

    /**
     * @param nodeIndex Index value of a Node in {@link #gridCellContent}.
     * @return The node at the provided index if provided path is in range.
     * else it will return {@code null}.
     */
    public Node getNodeAt(final int[] nodeIndex) {
        int rowIndex = 0;
        int colIndex = 1;
        GridPath e = new GridPath();
        if (e.isValidMinorPath(nodeIndex)) {
            return gridCellContent[nodeIndex[rowIndex]][nodeIndex[colIndex]];
        }
        return null;
    }

    /**
     * Attempts to add all valid Minor Paths from {@link #gridCellContent},
     * regardless of if the Cell content is {@code null} or not.
     *
     * Note:
     * <ul>
     *     <li>Will return the Cell content at of all valid minor paths (if
     *     some are not valid they will be skipped)</li>
     *     <li>Index values are only checked via
     *     {@link GridPath#isValidMinorPath(int[])} which means that some
     *     values can be above the Cell value size.</li>
     *     <li>An ArrayList is returned regardless of if the input
     *     parameters are invalid.</li>
     * </ul>
     *
     * @param fullPath The full path to add cell content from.
     * @return An ArrayList of all the cell which hold true to the above
     * listed.
     *
     */
    public ArrayList<Node> getCellContentFromPath(final ArrayList<int[]>
                                                          fullPath) {
        GridPath e = new GridPath();
        ArrayList<Node> cellContent = new ArrayList<>();
        final int rowIndex = 0;
        final int colIndex = 1;

        //Grab all minor paths which are valid
        for (int[] minorPath : fullPath) {
            if (e.isValidMinorPath(minorPath)) {
                final int row = minorPath[rowIndex];
                final int col = minorPath[colIndex];
                cellContent.add(this.gridCellContent[row][col]);
            }
        }

        return cellContent;
    }
}
