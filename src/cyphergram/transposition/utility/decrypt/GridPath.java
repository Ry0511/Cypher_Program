package cyphergram.transposition.utility.decrypt;

import java.lang.reflect.MalformedParametersException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Recreation of the
 * {@link cyphergram.transposition.utility.encrypt.GridPath} class but with less
 * "free-balling-it" aspects since I eye-balled 100% of that Class and don't
 * want to look at it.
 *
 * @author -Ry
 * @version 0.2
 * Copyright: N/A
 */
public class GridPath {
    /**
     * Constant String error message used at: {@link #addPath(int[])} for
     * when {@link #isValidMinorPath(int[])} returns {@code false}.
     */
    private static final String INVALID_PATH_ARRAY = "Provided Array does not"
            + " meet the expected criteria...";
    /**
     * Path is stored as an ArrayList of Integer Arrays (Expected Array
     * format: Row, Col) and the path is designated by entry order to the
     * ArrayList (Like a Queue).
     */
    private final ArrayList<int[]> fullPath = new ArrayList<>();

    /**
     * Adds the provided path to the Path Array if the provided path is valid.
     *
     * @param minorPath The path to add to this GridPath. Expects an Array of
     *                 size 2 and format (row, col)
     * @throws MalformedParametersException if the provided path is invalid.
     */
    public void addPath(final int[] minorPath)
            throws MalformedParametersException {
        if (isValidMinorPath(minorPath)) {
            this.fullPath.add(minorPath);
        } else {
            throw new MalformedParametersException(INVALID_PATH_ARRAY);
        }
    }

    /**
     * @return The full path stored in {@link #fullPath} regardless of what
     * it contains.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        this.fullPath.forEach(i -> sb.append(Arrays.toString(i)).append(" "));
        return sb.toString();
    }

    /**
     * @return {@link #fullPath} Regardless of its content state.
     */
    public ArrayList<int[]> getFullPath() {
        return this.fullPath;
    }

    /**
     * @return The size of: {@link #fullPath} ArrayList. (Yields same value
     * as {@code getFullPath().size()})
     */
    public int getPathSize() {
        return this.fullPath.size();
    }

    /**
     * Checks to see if the provided path is setup correctly.
     * <p>
     * Note a valid path is setup correctly if and only if the following
     * hold true:
     * <ul>
     *     <li>Provided Path Array Size is two.</li>
     *     <li>Row Index Value (Zeroth index) is greater than or equal to
     *     Zero.</li>
     *     <li>Col Index Value (First index) is greater than or equal to
     *     Zero.</li>
     * </ul>
     *
     * @param minorPath The minor path to verify if the above holds true or not.
     * @return {@code true} if the above holds. else if one does not hold
     * then the returned value is {@code false}.
     */
    public boolean isValidMinorPath(final int[] minorPath) {
        //Setup data
        final int rowIndex = 0;
        final int colIndex = 1;
        final int minAcceptedValue = 0;
        final int expectedSize = 2;

        //Apply checks
        boolean sizeValid = minorPath.length == expectedSize;
        boolean rowValid = minorPath[rowIndex] >= minAcceptedValue;
        boolean colValid = minorPath[colIndex] >= minAcceptedValue;

        return sizeValid && rowValid && colValid;
    }

    /**
     * Breaks the currently stored path at the provided Minor Path,
     * effectively backtracking 'n' amount of nodes.
     * <p>
     *
     * @param minorPath The Minor Path to break this Grid Path at.
     */
    public void breakPathAt(final int[] minorPath) {
        int minorPathIndex = this.indexOfMinorPath(minorPath);
        final int maxIndex = this.fullPath.size();
        final int minimumIndex = 0;

        //Ensure Path contains provided minorPath
        if (minorPathIndex >= minimumIndex) {
            List<int[]> brokenElements = this.fullPath.subList(minorPathIndex,
                    maxIndex);
            removeAllEquivalent(brokenElements);
        }
    }

    /**
     * Removes all equivalent int[] from the current GridPath. (Will remove
     * if and only if {@link #pathContains} returns {@code true}).
     *
     * @param minorPaths The elements to remove from the path if they are in
     *                   the path.
     */
    private void removeAllEquivalent(final List<int[]> minorPaths) {
        ArrayList<int[]> elementsToDelete = new ArrayList<>();

        //Find elements to delete
        for (int[] minorPath : minorPaths) {
            if (this.pathContains(minorPath)) {
                elementsToDelete.add(minorPath);
            }
        }

        this.fullPath.removeAll(elementsToDelete);
    }

    /**
     * @param minorPath The minor path to check for existence inside of
     *                  {@link #fullPath}
     * @return {@code true} if the provided minor path matches a minor path
     * in the full path array. (If {@link #isMinorPathEqual(int[], int[])})
     * yields {@code true} for at least one of 'n' inside of {@link #fullPath}.
     * <p><br>
     * Note: this is O(n) for time complexity as ordering and other
     * optimisations are NOT done.
     */
    public boolean pathContains(final int[] minorPath) {
        //Doesn't order the Array so time complexity is O(n)
        for (int[] thisMinorPath : this.fullPath) {
            if (this.isMinorPathEqual(minorPath, thisMinorPath)) {
                return true;
            }
        }
        //Default exit
        return false;
    }

    /**
     * Checks to see if the provided paths are equal to one another (They
     * refer to the same position).
     * <p>
     *
     * @param minorA Minor path to check for equivalence to.
     * @param minorB Secondary path to check for equivalence to.
     * @return {@code true} If the provided Minor Paths refer to the same
     * position on a grid. (Cartesian Co-ordinates are equal). else will
     * return {@code false}.
     */
    public boolean isMinorPathEqual(final int[] minorA, final int[] minorB) {
        final int rowIndex = 0;
        final int colIndex = 1;
        //Ensure they're valid Minor Paths
        if (isValidMinorPath(minorA) && isValidMinorPath(minorB)) {
            boolean rowSame = minorA[rowIndex] == minorB[rowIndex];
            boolean colSame = minorA[colIndex] == minorB[colIndex];
            return rowSame && colSame;

            //If they're not valid minor paths return false. (default)
        } else {
            return false;
        }
    }

    /**
     * Searches the Path Iteratively until it finds the provided Minor Path
     * or it runs out of elements to search through.
     *
     * @param minorPath Minor path to find in {@link #fullPath}.
     * @return The index value of the Minor Path in {@link #fullPath} if
     * found. But if the Minor Path could not be found then the default
     * return value if -1.
     */
    public int indexOfMinorPath(final int[] minorPath) {
        final int rowIndex = 0;
        final int colIndex = 1;
        int minorPathIndex = 0;
        //Find minor path.
        if (isValidMinorPath(minorPath)) {
            for (int[] thisMinorPath : this.fullPath) {
                //Ensure they point to the same row, col.
                boolean rowSame =
                        thisMinorPath[rowIndex] == minorPath[rowIndex];
                boolean colSame =
                        thisMinorPath[colIndex] == minorPath[colIndex];

                //Return index found at
                if (rowSame && colSame) {
                    return minorPathIndex;
                } else {
                    minorPathIndex++;
                }
            }
        }

        //-1 is error/default exit in-case of non existent path.
        return -1;
    }
}
