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

        try(Reader in = new FileReader(file)){
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
        }
        return table;
    }
    /**
     * Helper function that returns whether a table is rectangular. Requires 'table' to be a
     * sequence of a sequence of strings.
     */
    private static boolean checkRectangular(Seq<Seq<String>> table){
        int nColumns = 0;
        // Looping through rows
        for(int rowIndex = 0; rowIndex < table.size(); rowIndex++){
            // Looping through columns
            int nColumnsThis = 0;
            for(int colIndex = 0; colIndex < table.get(rowIndex).size(); colIndex++){
                nColumnsThis++;
            }
            if (nColumns == 0){
                nColumns = nColumnsThis;
            }
            if (nColumnsThis != nColumns){
                return false;
            }
        }
        return true;
    }

    /**
     * Return the left outer join of tables `left` and `right`, joined on their first column. Result
     * will represent a rectangular table, with empty strings filling in any columns from `right`
     * when there is no match. Requires that `left` and `right` represent rectangular tables with at
     * least 1 column.
     */
    public static Seq<Seq<String>> join(Seq<Seq<String>> left, Seq<Seq<String>> right){
        assert checkRectangular(left);
        assert checkRectangular(right);
        Seq<Seq<String>> mergedList = new LinkedSeq<>();
        // left.size() is # of rows in left table
        for (int rowIndexLeft = 0; rowIndexLeft < left.size(); rowIndexLeft++){
            Seq<String> mergedListRow = new LinkedSeq<>();
            Seq<String> currentRowLeft = left.get(rowIndexLeft);
            boolean addedOtherRow = false;
            // Each row from left table will always appear in the final merged list
            // CurrentRowLeft.size() is the # of columns in the current left row
            for (int colIndexLeft = 0; colIndexLeft < currentRowLeft.size();
                    colIndexLeft++){
                mergedListRow.append(currentRowLeft.get(colIndexLeft));
            }
            // right.size() is # of rows in right table
            for(int rowIndexRight = 0; rowIndexRight < right.size(); rowIndexRight++){
                Seq<String> currentRowRight = right.get(rowIndexRight);
                // if a first column of right row matches with a first column of left row
                // add remaining elements of right row to mergedListRow
                if(currentRowLeft.get(0).equals(currentRowRight.get(0))){
                    // CurrentRowRight.size() is the # of columns in the current right row
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
                    mergedListRow.append("");
                }
                mergedList.append(mergedListRow);
            }
        }
        assert checkRectangular((mergedList));
        return mergedList;

        /**
        //int indexThis = 0;
        //int indexOther = 0;
        //Seq<String> currentRowThis = left.get(indexThis);
        //Seq<String> currentRowOther = right.get(indexOther);
        while (currentRowThis != null){
            Seq<String> mergedListRow = new LinkedSeq<>();
            // adding left row's elements to mergedList's row
            for (int i = 0; i < currentRowThis.size(); i++){
                mergedListRow.append(currentRowThis.get(i));
            }
            while(currentRowOther != null){
                if(currentRowThis.get(0).equals(currentRowOther.get(0))){
                    // TODO parse elements into mergedList
                    // adding right row's elements to mergedList's row
                    for (int i = 1; i < currentRowOther.size(); i++){
                        mergedListRow.append(currentRowOther.get(i));
                    }
                }
                indexOther++;
                currentRowOther = right.get(indexOther);
            }
            // add mergedList row after 2nd while loop; if statement could have been skipped
            mergedList.append(mergedListRow);
            indexThis++;
            currentRowThis = left.get(indexThis);
        }
//        for(int i = 0; i < left.size();i++){
//            for(int j = 0; j < right.size();j++){
//            }
//        }
        return mergedList; */
    }
    //public static void main(String[] args){
//File input = new File("C:\\Users\\lamle\\CS2110\\a3\\input-test\\example");}
}
