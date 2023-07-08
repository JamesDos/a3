package cs2110;

import java.util.*;
import java.io.*;

public class CsvJoin {

    /**
     * Load a table from a Simplified CSV file and return a row-major list-of-lists representation.
     * The CSV file is assumed to be in the platform's default encoding. Throws an IOException if
     * there is a problem reading the file.
     */
    public static Seq<Seq<String>> csvToList(String file) throws IOException {
        assert file != null;
        Seq<Seq<String>> table = new LinkedSeq<>();
        Reader in = new FileReader(file);
        Scanner lines = new Scanner(in);
        while (lines.hasNextLine()) {
            // initialize line var to reference Scanner
            String line = lines.nextLine();
            // initialize lineArray to reference line.split
            String[] lineArray = line.split(",", -1);
            // initialize row linked list to be sparse with lineArray
            Seq<String> row = new LinkedSeq<>();
            for (String word : lineArray) {
                // parse word into each element of Seq<String>
                row.append(word);
            }
            // add row into columns list
            table.append(row);
        }
        return table;
    }

    /**
     * Function that returns whether a table is both rectangular and has columns. Returns false if
     * 'table' is either non-rectangular, has no columns, or is empty Requires 'table' to be a
     * sequence of a sequence of strings.
     */
    private static boolean checkRectangular(Seq<Seq<String>> table) {
        assert table != null;
        // Checks if table has rows
        if (table.size() == 0) {
            return false;
        }
        // nColumns is how many columns each row is supposed to have
        int nColumns = table.get(0).size();
        for (Seq<String> row : table) {
            if (row.size() != nColumns || row.size() == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Helper function that appends elements from 'otherRow' to 'thisRow'; modifies 'thisRow'
     * Returns the sequence of strings 'thisRow' after the appending. Requires 'thisRow' and
     * 'otherRow' to be a sequence of strings
     */

    private static Seq<String> appendToRow(Seq<String> thisRow, Seq<String> otherRow) {
        assert thisRow != null;
        assert otherRow != null;
        for (String s : otherRow) {
            thisRow.append(s);
        }
        return thisRow;
    }

    /**
     * Return the left outer join of tables `left` and `right`, joined on their first column. Result
     * will represent a rectangular table, with empty strings filling in any columns from `right`
     * when there is no match. Requires that `left` and `right` represent rectangular tables with at
     * least 1 column.
     */
    public static Seq<Seq<String>> join(Seq<Seq<String>> left, Seq<Seq<String>> right) {
        assert left != null;
        assert right != null;
        assert checkRectangular(left);
        assert checkRectangular(right);
        Seq<Seq<String>> mergedList = new LinkedSeq<>();
        for (Seq<String> currentRowLeft : left) {
            Seq<String> mergedListRowLeft = new LinkedSeq<>();
            boolean addedOtherRow = false;
            // Each row from left table will always appear in the final merged list
            appendToRow(mergedListRowLeft, currentRowLeft);
            for (Seq<String> currentRowRight : right) {
                Seq<String> mergedListRow = new LinkedSeq<>();
                appendToRow(mergedListRow, mergedListRowLeft);
                // if a first column of right row matches with a first column of left row
                // add remaining elements of right row to mergedListRow
                if (currentRowLeft.get(0).equals(currentRowRight.get(0))) {
                    for (int colIndexRight = 1; colIndexRight < currentRowRight.size();
                            colIndexRight++) {
                        mergedListRow.append(currentRowRight.get(colIndexRight));
                        addedOtherRow = true;
                    }
                    mergedList.append(mergedListRow);
                }
            }
            // None of the elements from right row were added to final mergedListRow
            // Must fill remaining spots with empty strings
            if (!addedOtherRow) {
                for (int i = 1; i < right.get(0).size(); i++) {
                    mergedListRowLeft.append("");
                }
                mergedList.append(mergedListRowLeft);
            }
        }
        assert checkRectangular((mergedList));
        return mergedList;
    }

    /**
     * Helper method used by main() that prints out a table in a simplified CSV format. mergedTable
     * is a table made from joining two input tables in CsvJoinHelper().
     */

    private static void CsvFormatter(Seq<Seq<String>> mergedTable) {
        for (Seq<String> row : mergedTable) {
            String printString = "";
            for (String s : row) {
                printString += s + ",";
            }
            System.out.println(printString.substring(0, (printString.length() - 1)));
        }
    }


    public static void main(String[] args) {
        try {
            if (args.length != 2) {
                System.err.println("Error: must provide exactly two arguments");
                System.exit(-1);
            }
            Seq<Seq<String>> left = csvToList(args[0]);
            Seq<Seq<String>> right = csvToList(args[1]);
            if (!checkRectangular(left) || !checkRectangular(right)) {
                System.err.println("Error: Input tables are not rectangular or have no columns");
                System.exit(-1);
            }
            Seq<Seq<String>> mergedTable = join(left, right);
            CsvFormatter(mergedTable);
        } catch (java.io.IOException e) {
            System.err.println("Error: Could not read input tables.");
            System.err.println(e);
            System.exit(-1);
        }
    }
}
