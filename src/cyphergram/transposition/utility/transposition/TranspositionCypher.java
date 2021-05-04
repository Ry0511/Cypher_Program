package cyphergram.transposition.utility.transposition;

import cyphergram.transposition.utility.general.GeneralUtils;

import java.util.ArrayList;

/**
 * Currently a basic Transposition Cipher which allows traversal via Paths of
 * ArrayList<int[]>.
 *
 * @author -Ry
 * @version 0.1
 * Copyright: N/A
 */
public class TranspositionCypher {
    /**
     * Grid which contains either just the input String or a mix of both the
     * input String and the provided pad key.
     */
    private final String[][] grid;

    /**
     * Creates a Transposition Cypher and Maps the provided String Naturally
     * using {@link GeneralUtils#mapNaturally(String[][], String, String)}.
     *
     * @param input  The input String to map to a Grid.
     * @param padKey The pad key to use once the String is out of elements
     *               and the grid isn't filled.
     * @param rows   The number of Rows the Grid should have.
     * @param cols   The number of Columns the Grid should have.
     */
    public TranspositionCypher(final String input, final String padKey,
                               final int rows, final int cols) {
        //Method updates the reference of this.cell
        this.grid = GeneralUtils.mapNaturally(new String[rows][cols], input,
                padKey);
    }

    /**
     * Parses each Array from the provided Full Path to a position inside of
     * {@link #grid}.
     * <p>
     * Note this method only checks for Valid Paths via
     * {@link GeneralUtils#isMinorPathValid(int[])} and only this.
     *
     * @param fullPath Full path to parse and follow.
     * @return String of all the cell content from the provided path.
     */
    public String decryptWithPath(final ArrayList<int[]> fullPath) {
        StringBuilder sb = new StringBuilder();
        final int rowIndex = 0;
        final int colIndex = 1;
        for (int[] minorPath : fullPath) {
            if (GeneralUtils.isMinorPathValid(minorPath)) {
                final int row = minorPath[rowIndex];
                final int col = minorPath[colIndex];
                sb.append(grid[row][col]);
            }
        }
        return sb.toString();
    }
}
