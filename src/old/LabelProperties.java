package old;
/**
 * Label State properties to define extra properties to a label and store
 * them, does so without the label needing to be provided.
 *
 * @author -Ry
 * @version 0.1
 * Copyright: N/A
 */
public class LabelProperties {
    /**
     * Original co-ordinates of the label, this won't change and is fixed.
     */
    private final String coordinates;
    private boolean clickedState;
    private boolean isPadLabel;

    /**
     * Define our label properties providing co-ordinates, and a current state.
     *
     * @param cords    The GridBox co-ordinates of this label. Expected: "(2,
     *                 1)"
     * @param curState The current state of this label (used to indicate
     *                 single state changes)
     */
    public LabelProperties(String cords, boolean curState) {
        this.coordinates = cords;
        this.clickedState = curState;
    }

    /**
     * @return Provided String co-ordinates at construction.
     */
    public String getCoordinates() {
        return this.coordinates;
    }

    /**
     * Parses the column value of the provided co-ordinates and returns the
     * result. This assumes that the {@link #coordinates) are setup correctly
     * in the format "(2, 1)".
     *
     * @return Parsed int of the expected colValIndex, Above would return '2'.
     */
    public int getCol() {
        final int colValIndex = 1;
        return Integer.parseInt(String.valueOf(coordinates
                .charAt(colValIndex)));
    }

    /**
     * Parses the row value of the provided co-ordinates and returns the
     * result. This assumes that the {@link #coordinates) are setup correctly
     * in the format "(2, 1)".
     *
     * @return Parsed int of the expected rowValIndex, Above would return '1'.
     */
    public int getRow() {
        final int rowValIndex = 4;
        return Integer.parseInt(String.valueOf(coordinates
                .charAt(rowValIndex)));
    }

    /**
     * @return The state of this property.
     */
    public boolean getClickedState() {
        return this.clickedState;
    }

    /**
     * Set the state of this property to the provided state.
     *
     * @param newState The provided state to assign as the new state.
     */
    public void setClickedState(boolean newState) {
        this.clickedState = newState;
    }

    /**
     * @return {@code true} if the {@link #isPadLabel} state is true. Else it
     * will return {@code false}.
     */
    public boolean isPadLabel() {
        return this.isPadLabel;
    }

    /**
     * Updates the properties of {@link #isPadLabel} to the provided state.
     *
     * @param state The provided state to set the pad label state to.
     */
    public void setPadLabel(boolean state) {
        this.isPadLabel = state;
    }
}
