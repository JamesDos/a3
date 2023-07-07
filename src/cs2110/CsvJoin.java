package cs2110;
import java.util.*;
import java.io.*;
public class CsvJoin {

    /**
     * Load a table from a Simplified CSV file and return a row-major list-of-lists representation.
     * The CSV file is assumed to be in the platform's default encoding. Throws an IOException if
     * there is a problem reading the file.
     */
    public static Seq<Seq<String>> csvToList(String file) throws IOException{
        //TODO check for handeling of empty file (",,,," or "" file)
        Seq<Seq<String>> table = new LinkedSeq<>();
        Reader in = new FileReader(file);
        Scanner lines = new Scanner(in);
        while(lines.hasNextLine()){
        // initialize line var to reference Scanner
        String line = lines.nextLine();
        // initialize lineArray to reference line.split
        String[] lineArray = line.split(",", -1);
        // initialize row linked list to be sparse with lineArray
        Seq<String> row = new LinkedSeq<>();
            for(String word: lineArray){
            // parse word into each element of Seq<String>
            row.append(word);
            }
        // add row into columns list
        table.append(row);
        }
        return table;
    }
    /**
     * Helper function that returns whether a table is rectangular. Requires 'table' to be a
     * sequence of a sequence of strings.
     */
    private static boolean checkRectangular(Seq<Seq<String>> table){
        assert table != null;
        int nColumns = 0;
        // Looping through rows
        for(Seq<String> row: table){
            // Looping through columns
            int nColumnsThis = 0;
            for(String column: row){
                if (!column.isEmpty()) {
                    nColumnsThis++;
                }
            }
            if (nColumns == 0){
                nColumns = nColumnsThis;
            }
            if (nColumnsThis != nColumns){
                return false;
            }
        }
        return nColumns != 0;
    }

    /**
     * Helper function that appends elements from 'otherRow' to 'thisRow'; modifies 'thisRow'
     * Returns the sequence of strings 'thisRow' after the appending.
     * Requires 'thisRow' and 'otherRow' to be a sequence of strings
     */

    private static Seq<String> appendToRow(Seq<String> thisRow, Seq<String> otherRow){
        assert thisRow != null;
        assert otherRow != null;
        for(String s: otherRow){
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
    public static Seq<Seq<String>> join(Seq<Seq<String>> left, Seq<Seq<String>> right){
        assert left != null;
        assert right != null;
        assert checkRectangular(left);
        assert checkRectangular(right);
        Seq<Seq<String>> mergedList = new LinkedSeq<>();
        for (Seq<String> currentRowLeft: left){
            Seq<String> mergedListRowLeft = new LinkedSeq<>();
            boolean addedOtherRow = false;
            // Each row from left table will always appear in the final merged list
            appendToRow(mergedListRowLeft, currentRowLeft);
            for(Seq<String> currentRowRight: right){
                Seq<String> mergedListRow = new LinkedSeq<>();
                appendToRow(mergedListRow, mergedListRowLeft);
                // if a first column of right row matches with a first column of left row
                // add remaining elements of right row to mergedListRow
                if(currentRowLeft.get(0).equals(currentRowRight.get(0))){
                    for (int colIndexRight = 1; colIndexRight < currentRowRight.size();
                            colIndexRight++){
                        mergedListRow.append(currentRowRight.get(colIndexRight));
                        addedOtherRow = true;
                    }
                    mergedList.append(mergedListRow);
                }
            }
            // None of the elements from right row were added to final mergedListRow
            // Must fill remaining spots with empty strings
            if (!addedOtherRow){
                for(int i = 1; i < right.get(0).size(); i++){
                    mergedListRowLeft.append("");
                }
                mergedList.append(mergedListRowLeft);
            }
        }
        assert checkRectangular((mergedList));
        return mergedList;
    }

    /**
     * Helper method used by main() that joins tables from "input1.csv" and "input2.csv" based
     * on which directory, dir, they are in.
     * Prints error if either tables are not rectangular or have no columns.
     */

    private static void CsvJoinHelper(String dir) throws IOException {
        Seq<Seq<String>> left = csvToList("input-tests/" + dir + "/input1.csv");
        Seq<Seq<String>> right = csvToList("input-tests/" + dir + "/input2.csv");
        //Seq<Seq<String>> right = csvToList("tests/testCsvToList/no-cols.csv");
        if (!checkRectangular(left) || !checkRectangular(right)) {
            System.err.println("Error: Input tables are not rectangular or have no columns");
            System.exit(-1);
        }
        Seq<Seq<String>> mergedTable = join(left, right);
        CsvFormatter(mergedTable);
    }

    /**
     * Helper method used by CsvJoinHelper() that prints out a table in a simplified CSV format.
     * mergedTable is a table made from joining two input tables in CsvJoinHelper().
     */

    private static void CsvFormatter(Seq<Seq<String>> mergedTable){
        for(Seq<String> row:mergedTable){
            String printString = "";
            for(String s:row){
                        printString += s + ",";
            }
            System.out.println(printString.substring(0,(printString.length()-1)));
        }
    }


    public static void main(String[] args){
        try{
            // TODO: Maybe put code in CsvJoinHelper in main since it is not directly
            // reading from main's inputs (input 1 and 2 are hard coded)
            // Maybe can configure main to take in the path of input 1 and 2
            CsvJoinHelper("example");
        } catch(java.io.IOException e){
            System.err.println("Error: Could not read input tables.");
            System.err.println(e);
            System.exit(-1);
        }
    }
}
