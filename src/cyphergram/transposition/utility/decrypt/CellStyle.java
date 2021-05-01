package cyphergram.transposition.utility.decrypt;

import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

/**
 * Defines a style set for Column and Row Constraints which can be applied to
 * any provided RowConstraint or ColumnConstraint. This class assumes that
 * the StyleSet for Row and Constraint are the same and as such will apply
 * the same to both.
 *
 * NOTE THIS CLASS USES DEFAULT CONSTRUCTOR.
 *
 * @author -Ry
 * @version 0.1
 * Copyright: N/A
 */
public class CellStyle {
    /**
     * Enum to specify generically the Alignment for the cell style.
     */
    public enum Alignment {
        CENTER,
        LEFT,
        RIGHT
    }
    /**
     * Specifies the Minimum Width/Height for the cell.
     */
    private double minVal;

    /**
     * Specifies the Preference Width/Height for the cell.
     */
    private double prefVal;

    /**
     * Specifies the Maximum Width/Height for the cell.
     */
    private double maxVal;

    /**
     * Specifies the Grow Priority of the cell.
     */
    private Priority growPriority;

    /**
     * Specifies the HPos for the Columns (tied to the same state as
     * {@link #vPos}).
     */
    private HPos hPos;

    /**
     * Specifies the VPos for the Rows. (Only applies a change for CENTER
     * Alignment).
     */
    private VPos vPos;

    /**
     * Specifies if the Cell should be filled or not.
     */
    private boolean fillState;

    /**
     * Specifies the percentage size of the cell.
     */
    private double percentSize;

    /**
     * Sets the minimum cell size to the provided size.
     *
     * @param min The minimum cell size for the GridPane.
     */
    public void setMinVal(final double min) {
        this.minVal = min;
    }

    /**
     * Sets the Preference cell size to the provided size.
     *
     * @param pref The minimum cell size for the GridPane.
     */
    public void setPrefVal(final double pref) {
        this.prefVal = pref;
    }

    /**
     * Sets the Maximum cell size to the provided size.
     *
     * @param max The minimum cell size for the GridPane.
     */
    public void setMaxVal(final double max) {
        this.maxVal = max;
    }

    /**
     * Sets the Grow priority of the Cell to the provided size.
     *
     * @param priority The Grow Priority to use on the cells.
     */
    public void setGrowPriority(final Priority priority) {
        this.growPriority = priority;
    }

    /**
     * Sets the cell alignment to the provided cell alignment.
     *
     * Note: VPos alignments should be set manually if you want full control.
     *
     * @param alignment The cell alignment of the GridPane
     */
    public void setCellAlignment(final Alignment alignment) {
        //Center
        if (alignment == Alignment.CENTER) {
            hPos = HPos.CENTER;
            vPos = VPos.CENTER;

            //Left
        } else if (alignment == Alignment.LEFT) {
            hPos = HPos.LEFT;
            vPos = VPos.BASELINE;

            //Right
        } else if (alignment == Alignment.RIGHT) {
            hPos = HPos.RIGHT;
            vPos = VPos.BASELINE;
        }
    }

    /**
     * Sets the fill state for the cells in the GridPane to the provided state.
     *
     * @param fill The provided fill state to use.
     */
    public void setFillState(final boolean fill) {
        this.fillState = fill;
    }

    /**
     * Sets the percentage size for the cells in the GridPane to the provided
     * size.
     *
     * @param size The percentage size to use for the Cells in the GridPane.
     */
    public void setPercentSize(final double size) {
        this.percentSize = size;
    }

    /**
     * Once called this method will apply the stored/set states for all the
     * RowConstraints and the ColumnConstraints. Due not that if you haven't
     * set anything for a state then that state won't change. Only the states
     * which have changed, will change.
     *
     * @param gridPane The GridPane to Apply the style to.
     */
    public void applyStyleToGrid(final GridPane gridPane) {
        setColumnStyle(gridPane.getColumnConstraints());
        setRowStyle(gridPane.getRowConstraints());
    }

    /**
     * Applies styles to every column in the provided List.
     *
     * @param columns The List of Columns to apply styles to.
     */
    private void setColumnStyle(final ObservableList<ColumnConstraints>
                                        columns) {
        for (ColumnConstraints e : columns) {
            //Defaults
            e.setMinWidth(this.minVal);
            e.setPrefWidth(this.prefVal);
            e.setMaxWidth(this.maxVal);

            //Sizing style
            e.setHgrow(this.growPriority);
            e.setHalignment(this.hPos);

            //Extra style
            e.setFillWidth(this.fillState);
            e.setPercentWidth(this.percentSize);
        }
    }

    /**
     * Applies styles to every row in the provided List.
     *
     * @param rows The List of Rows to apply styles to.
     */
    private void setRowStyle(final ObservableList<RowConstraints> rows) {
        for (RowConstraints e : rows) {
            //Defaults
            e.setMinHeight(this.minVal);
            e.setPrefHeight(this.prefVal);
            e.setMaxHeight(this.maxVal);

            //Sizing style
            e.setVgrow(this.growPriority);
            e.setValignment(this.vPos);

            //Extra style
            e.setFillHeight(this.fillState);
            e.setPercentHeight(this.percentSize);
        }
    }
}
