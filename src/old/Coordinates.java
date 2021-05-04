package old;

/**
 * Co-ordinate value object which allows for the storage of abstract integers
 * values into cartesian co-ordinate values (x, y) Note this doesn't store z
 * axis.
 *
 * @author -Ry
 * @version 1.0
 * Copyright: N/A
 */
public class Coordinates<T> {
    /**
     * x value co-ordinates (refers to the columns)
     */
    private int x;

    /**
     * y value co-ordinates (refers to the rows)
     */
    private int y;

    /**
     * Element stored/referenced at this co-ordinate
     */
    private T element;

    /**
     * Construct co-ordinate values from x and y input integer values.
     * Note x refers to columns and y refers to rows.
     *
     * @param x x Co-ordinate -> Column Index Value.
     * @param y y Co-ordinate -> Row Index Value.
     */
    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
        element = null;
    }

    /**
     * @return {@code true} if the {@link #x} and {@link #y} coordinates of
     * this object and the provided object, match exactly (refer to the same
     * position). Else returns {@code false}
     */
    public boolean equals(Coordinates<T> e) {
        boolean isXSame = this.x == e.getX();
        boolean isYSame = this.y == e.getY();
        return isXSame && isYSame;
    }

    /**
     * @return Formatted string containing the x, and y, co-ordinates.
     */
    public String toString() {
        return String.format("(%s, %s)", x, y);
    }

    /**
     * @return {@link #x} co-ordinate.
     */
    public int getX() {
        return this.x;
    }

    /**
     * Set {@link #x} co-ordinate to the provided value.
     *
     * @param x The provided value to set the x co-ordinate value to.
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * @return {@link #y} co-ordinate.
     */
    public int getY() {
        return this.y;
    }

    /**
     * Set {@link #y} co-ordinate to the provided value.
     *
     * @param y The provided value to set the y co-ordinate value to.
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * @return The element stored at {@link #element} for this co-ordinate.
     */
    public T getElement() {
        return this.element;
    }

    /**
     * Sets the element at {@link #element} to the provided element.
     *
     * @param elem The provided element to store at these co-ordinates.
     */
    public void setElement(T elem) {
        this.element = elem;
    }
}
