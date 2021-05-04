package cyphergram.transposition.utility.general;

import cyphergram.transposition.utility.transposition.StringIterator;

/**
 * Utility Functions for frequently used operations.
 *
 * @author -Ry
 * @version 0.1
 * Copyright: N/A
 */
public final class GeneralUtils {

    /**
     * Hide default constructor.
     */
    private GeneralUtils() {
    }

    /**
     * Maps the String key to the base in the Natural Reading Order of Left
     * to Right.
     *
     * @param base Base to map the key to. (Grid of size x,y)
     * @param key  The key String to map to the Base.
     * @param padKey The padding key for excess Cells (if overflow of grid
     *               occurs this is used).
     *
     * @return Populated 2D Array of the provided String fitting in the
     * natural order with all blanks filled with the pad key.
     */
    public static String[][] mapNaturally(final String[][] base,
                                          final String key,
                                          final String padKey) {
        StringIterator e = new StringIterator(key);
        for (int row = 0; row < base.length; row++) {
            for (int col = 0; col < base[row].length; col++) {
                if (e.hasNext()) {
                    base[row][col] = e.next();
                } else {
                    base[row][col] = padKey;
                }
            }
        }
        return base;
    }

    /**
     * Checks to see if the provided Minor Path has a valid Size and index.
     * (Only checks for X >= 0)
     *
     * @param minorPath The minor path to validate.
     *
     * @return {@code true} if the Provided Minor Path is of Size 2 and both
     * values are greater than or equal to Zero. Else will return {@code false}.
     */
    public static boolean isMinorPathValid(final int[] minorPath) {
        final int rowIndex = 0;
        final int colIndex = 1;
        final int expectedSize = 2;

        if (minorPath.length == expectedSize) {
            boolean validRow = minorPath[rowIndex] >= 0;
            boolean validCol = minorPath[colIndex] >= 0;
            return validRow && validCol;
        } else {
            return false;
        }
    }
}
