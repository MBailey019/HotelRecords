package hotelrecords;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Helper Class for working with records of sales.
 *
 * Contains a function for writing flat-file sales records into an ArrayList, totaling sales by category, and writing sales log by category. 
 * 
 * <h2>CS&143 S1</h2>
 * <h2>Program1 File I/O</h2>
 * <h3>1/13/2015</h3>
 *
 * @author Matt Bailey    
 */
public class Records {
    
    /**
    * Reads flat file sales records into ArrayList      
    *
    * @param filename the name of the file to read.
    * 
    * @param linesToSkip an array of lines to linesToSkip. Used for error handling.
    * 
    * @return    The ArrayList
     * @throws FileNotFoundException thrown by Scanner
     * @throws BadDataException thrown when an error occurs in the formatting of the flat file,
    * 
    * @pre  in flat-file:
    * Name must not contain numbers.
    * Price must begin with '$'.
    * Date must be in format 'mm/dd/yy'.
    */
    public static List<List<String>> readFileToArray(String filename, ArrayList<Integer> linesToSkip) throws FileNotFoundException, BadDataException{
        
        List<List<String>> records = new ArrayList<>(4);
        for (int i = 0; i<4; i++){
            records.add( new ArrayList<String>(1) );
        }
        File inputFile = new File(filename);
        Scanner flatFileRecords = new Scanner(inputFile);
        Integer currentLineNumber = 1;
        String errorMessage = "";
        while (flatFileRecords.hasNextLine()){
            String currentLine = flatFileRecords.nextLine();
            if (!linesToSkip.contains((Integer)currentLineNumber)){
                boolean isProperFormat = true;
                ArrayList<String> lineEntry = new ArrayList<String>(); 
                Scanner lineScanner = new Scanner(currentLine).useDelimiter(";");
                for (int i=0; i<4; i++){
                    if (!lineScanner.hasNext()){
                        isProperFormat = false;
                        errorMessage = "Incomplete entry on line "+currentLineNumber+".";
                    } else {
                        String item = lineScanner.next().trim();
                        switch (i){
                            case (0): if (item.contains("[0-9]")){
                                isProperFormat = false;
                                errorMessage = "Name format error on line "+currentLineNumber+":\n Name must not contain numerical values";
                            }
                            break;
                            case (1): 
                            break;
                            case (2): if (!item.matches("^\\$[0-9.]*")) {
                                isProperFormat = false;
                                errorMessage = "Price format error on line "+currentLineNumber+":\n Price must begin with \"$\"";
                            }
                            break;
                            case (3): if (!item.matches("\\s*\\d\\d/\\d\\d/\\d\\d")){
                                isProperFormat = false;
                                errorMessage = "Date format error on line "+currentLineNumber+":\n Date must be in format dd/mm/yy";
                            }
                            break;
                        }
                        if (isProperFormat) lineEntry.add(item);
                    }
                }
                if (!isProperFormat){
                    throw new BadDataException(currentLineNumber, errorMessage);
                } else {
                    for (int i = 0; i<4; i++){
                        records.get(i).add(lineEntry.get(i));
                    } 
                }
                lineScanner.close();
            }
            currentLineNumber++;
        }
        flatFileRecords.close();
        return records;
    }
    
    /**
     * condenses a string down to only letters for comparison that neglects punctuation  
     * 
     * @param value the String to be cleaned
     * @return a "clean" string
     */
    public static String cleanString(String value){
        return value.replaceAll("[^a-zA-Z ]", "").toUpperCase().trim();
    }
    
    /**
     * Creates an ArrayList of unique values from a column in a multi-dimensional array.
     * 
     * @param records the array
     * @param column the column to analyze
     * @return an array of unique values
     */
    public static ArrayList<String> getUniqueValues(List<List<String>> records, int column){
        ArrayList<String> uniqueValues = new ArrayList();
        for (String value : records.get(column)){
            if (!uniqueValues.contains(cleanString(value).toUpperCase())){
                uniqueValues.add(cleanString(value).toUpperCase());
            }
        }
        return uniqueValues;
    }
    
    /**
    * Displays the total sales for each category of service    
    * 
    * @param filename the name of the file to read.
     * @throws FileNotFoundException thrown if user cancels dialog. exits program if thrown;
    *
    */
    public static void totalServices(String filename) throws FileNotFoundException{
        List<List<String>> records = new ArrayList();
        ArrayList<Integer> skip = new ArrayList();
        while (records.isEmpty()){
           try{
               records = readFileToArray(filename, skip );
           }catch (BadDataException e){
               System.err.println("Exception: "+e.getMsg());
               skip.add(e.getLineNumber());
           }
        }
        ArrayList<String> uniqueServices = Records.getUniqueValues(records, 1);
        for ( String service : uniqueServices){
            double total = 0;
            for (int i = 0; i<records.get(0).size(); i++){
                String columnValue = records.get(1).get(i);
                columnValue = cleanString(columnValue).toUpperCase();
                if (columnValue.equals(service)){
                    String value = (records.get(2).get(i)).replaceAll("\\$", "");
                    total += Double.parseDouble(value);
                }
            }
            System.out.printf(service+": $%-3.2f \n",total);
        }
    }
    
   /**
    * Creates text file for each service category listing each transaction in that category;   
    * 
    * @param filename the name of the file to read.
     * @throws FileNotFoundException thrown if user cancels dialog. exits program if thrown;
    *
    */
    public static void transactionsByService(String filename) throws FileNotFoundException{
        List<List<String>> records = new ArrayList();
        ArrayList<Integer> skip = new ArrayList();
        while (records.isEmpty()){
           try{
               records = Records.readFileToArray(filename, skip );
           }catch (BadDataException e){
               System.err.println("EXCEPTION: "+e.getMsg());
               skip.add(e.getLineNumber());
           }
        }
        ArrayList<String> uniqueServices = Records.getUniqueValues(records, 1);
        for ( String service : uniqueServices){
            String cleanService = cleanString(service).toLowerCase();
            PrintWriter out = new PrintWriter(cleanService+".txt");
            out.println(service.toUpperCase());
            out.printf("\n| %4s | %30s | %10s | %8s |\n", "Line", "Name","Price","Date");
            for (int i = 0; i<records.get(0).size(); i++){
                String columnValue = records.get(1).get(i);
                columnValue = cleanString(columnValue).toUpperCase();
                if (columnValue.equals(service)){
                    String name = records.get(0).get(i);
                    if (name.length()>30) name = name.substring(0,27)+"...";
                    Double price = Double.parseDouble((records.get(2).get(i)).replaceAll("\\$", ""));
                    String date = records.get(3).get(i);
                    out.printf("| %4d | %30.30s | $%-9.2f | %8.8s |\n", i, name, price, date);
                }
            }
            out.close();
        }
    }
}

