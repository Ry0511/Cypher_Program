package CypherGram.transposition.Utility;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Stores a path in the form of two co-ordinate values ((x, y) == (col, row))
 * This assumes the input order is the path used i.e., first input item is the
 * first output item.
 *
 * @author -Ry
 * @version 1.1
 * Copyright: N/A
 */
public class GridPath<T> {
    /**
     * ArrayList to store the path co-ordinate elements. This assumes that
     * the order is based on entry to the Array so (n -> n+1 (1, 2, 3, 4))
     * and there are no skips in indexing.
     */
    private ArrayList<Coordinates<T>> path = new ArrayList<>();

    /**
     * @return String containing all co-ordinates in this path.
     */
    public String toString() {
        StringBuilder output = new StringBuilder();
       for (Coordinates<T> cord : path) {
           output.append(cord.toString());
       }
       return output.toString();
    }

    /**
     * Adds a co-ordinate to the top of the path (follows queue like
     * implementation where n -> n+1 is the path.)
     *
     * @param e Co-ordinate object which stores x, and y co-ordinates.
     */
    public void addCoordinate(Coordinates<T> e) {
        path.add(e);
    }

    /**
     * @return ArrayList containing the current path stored in this object.
     */
    public ArrayList<Coordinates<T>> getPath() {
        return this.path;
    }

    /**
     * Sets the path at this object to the provided path.
     *
     * @param e The provided path to update this path to.
     */
    public void setPath(ArrayList<Coordinates<T>> e) {
        this.path = e;
    }

    /**
     * @return ArrayList containing the current path backwards. (Allows for
     * iteration from n -> n+1 with ease).
     * <p>
     * Note this makes a copy and returns the copy and doesn't modify the
     * existing. So {@link #path} remains the same.
     */
    public ArrayList<Coordinates<T>> getReversePath() {
        ArrayList<Coordinates<T>> temp = new ArrayList<>(this.path);
        Collections.reverse(temp);
        return temp;
    }

    /**
     * Removes the top element from {@link #path}
     */
    public void removeTop() {
        this.path.remove(path.size() - 1);
    }

    /**
     * Breaks the path at the provided coordinate, creating a new path, then
     * assigns this objects path to that new path.
     *
     * @param coord The coordinate to break the path up at, removing all
     *              after and including the provided cord from the pathing.
     */
    public void removeCoordinate(Coordinates<T> coord) {
        int count = 0;
        for (Coordinates<T> temp : path) {
            if (temp.equals(coord)) {
                this.path = new ArrayList<>(path.subList(0, count));
                return;
            }
            count++;
        }
    }

    /**
     * Iterates through all co-ordinates and returns the index of the
     * provided coordinates, if the co-ordinates exist in the path.
     *
     * @return -1 is the co-ordinates could not be found in the path. Else it
     * will return the index of the co-ordinate.
     */
    public int getCoordinateIndex(Coordinates<T> input) {
        int count = 0;
        for (Coordinates<T> temp : path) {
            if (temp.equals(input)) {
                return count;
            }
            count++;
        }
        return -1;
    }

    /**
     * @return The size of the co-ordinates ArrayList
     */
    public int length() {
        return path.size();
    }

    /**
     * @return {@code true} if the provided Co-ordinates are a in the Path
     * somewhere. else return {@code false}
     */
    public boolean contains(Coordinates<T> temp) {
        for (Coordinates<T> e : path) {
            if (e.equals(temp)) {
                return true;
            }
        }
        return false;
    }
}
