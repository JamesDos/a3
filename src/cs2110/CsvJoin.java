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
        Seq<Seq<String>> matrix = new LinkedSeq<>();

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
            matrix.append(row);
            }
        }
        return matrix;
    }
    /**
     * Return the left outer join of tables `left` and `right`, joined on their first column. Result
     * will represent a rectangular table, with empty strings filling in any columns from `right`
     * when there is no match. Requires that `left` and `right` represent rectangular tables with at
     * least 1 column.
     */
    public static Seq<Seq<String>> join(Seq<Seq<String>> left, Seq<Seq<String>> right){
        Seq<Seq<String>> mergedList = new LinkedSeq<>();
        int indexThis = 0;
        int indexOther =0;
        Seq<String> currentRowThis = left.get(indexThis);
        Seq<String> currentRowOther = right.get(indexOther);
        while (!currentRowThis.equals(null)){
            while(!currentRowOther.equals(null)){
                if(currentRowThis.get(0).equals(currentRowOther.get(0))){
                    // TODO parse elements into mergedList
                }
                indexOther++;
                currentRowOther = right.get(indexOther);
            }
            indexThis++;
            currentRowThis = left.get(indexThis);
        }
//        for(int i = 0; i < left.size();i++){
//            for(int j = 0; j < right.size();j++){
//            }
//        }
        return mergedList;
    }
    //public static void main(String[] args){
//File input = new File("C:\\Users\\lamle\\CS2110\\a3\\input-test\\example");}
}
