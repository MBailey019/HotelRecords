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
     * @param itemsPerLine
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
    public static List<List<String>> readFileToArray(String filename, int itemsPerLine, ArrayList<Integer> linesToSkip) throws FileNotFoundException, BadDataException{
        
        List<List<String>> processedRecords = new ArrayList<>(4);
        for (int i = 0; i<4; i++){
            processedRecords.add( new ArrayList<String>(1) );
        }
        File inputFile = new File(filename);
        Scanner flatFileRecordsReader = new Scanner(inputFile);
        Integer currentLineNumber = 1;
        String errorMessage = "";
        while (flatFileRecordsReader.hasNextLine()){
            String currentLine = flatFileRecordsReader.nextLine();
            if (!linesToSkip.contains((Integer)currentLineNumber)){
                boolean isProperFormat = true;
                ArrayList<String> currentLineArrayEntry = new ArrayList<>(); 
                Scanner currentLineReader = new Scanner(currentLine);
                currentLineReader.useDelimiter(";");
                for (int lineItem=0; lineItem<itemsPerLine; lineItem++){
                    if (!currentLineReader.hasNext()){
                        isProperFormat = false;
                        errorMessage = "Incomplete entry: line " + currentLineNumber;
                    } else {
                        String currentItem = currentLineReader.next().trim();
                        switch (lineItem){
                            case (0): if (currentItem.contains("[0-9]")){
                                isProperFormat = false;
                                errorMessage = "Name format error: line " + currentLineNumber;
                                errorMessage += ":\n Name must not contain numerical values";
                            }
                            break;
                            case (1): 
                            break;
                            case (2): if (!currentItem.matches("^\\$[0-9.]*")) {
                                isProperFormat = false;
                                errorMessage = "Price format error on line " + currentLineNumber;
                                errorMessage += ":\n Price must begin with \"$\"";
                            }
                            break;
                            case (3): if (!currentItem.matches("\\s*\\d\\d/\\d\\d/\\d\\d")){
                                isProperFormat = false;
                                errorMessage = "Date format error on line " + currentLineNumber;
                                errorMessage += ":\n Date must be in format dd/mm/yy";
                            }
                            break;
                        }
                        if (isProperFormat) currentLineArrayEntry.add(currentItem);
                    }
                }
                if (!isProperFormat){
                    throw new BadDataException(currentLineNumber, errorMessage);
                } else {
                    for (int itemIndex = 0; itemIndex<itemsPerLine; itemIndex++){
                        String lineItem = currentLineArrayEntry.get(itemIndex);
                        processedRecords.get(itemIndex).add(lineItem);
                    } 
                }
                currentLineReader.close();
            }
            currentLineNumber++;
        }
        flatFileRecordsReader.close();
        return processedRecords;
    }
    
    /**
     * condenses a string down to only letters for comparison that neglects punctuation
     * @param value the String to be cleaned
     * @return a "clean" string
     */
    public static String cleanString(String value){
        return value.replaceAll("[^a-zA-Z ]", "").toUpperCase().trim();
    }
    
    /**
     * Creates an ArrayList of unique values from a column in a multi-dimensional array.
     * 
     * @param records
     * @param focusColumn the column to analyze
     * @return an array of unique values
     */
    public static ArrayList<String> getUniqueValues(List<List<String>> records, int focusColumn){
        ArrayList<String> uniqueValues = new ArrayList();
        for (String value : records.get(focusColumn)){
            if (!uniqueValues.contains(cleanString(value).toUpperCase())){
                uniqueValues.add(cleanString(value).toUpperCase());
            }
        }
        return uniqueValues;
    }
    
    /**
    * Displays the total sales for each category of service    
    * 
     * @param salesRecords
     * @param uniqueServices
    *
    */
    public static void totalServices(List<List<String>> salesRecords, 
            ArrayList<String> uniqueServices){

        for ( String service : uniqueServices){
            double serviceTotal = 0;
            for (int lineIndex = 0; lineIndex<salesRecords.get(0).size(); lineIndex++){
                String columnValue = salesRecords.get(1).get(lineIndex);
                columnValue = cleanString(columnValue).toUpperCase();
                if (columnValue.equals(service)){
                    String value = (salesRecords.get(2).get(lineIndex)).replaceAll("\\$", "");
                    serviceTotal += Double.parseDouble(value);
                }
            }
            System.out.printf(service+": $%-3.2f \n",serviceTotal);
        }
    }
    
   /**
    * Creates text file for each service category listing each transaction in that category;   
    * 
     * @param salesRecords
     * @param uniqueServices
    *
    */
    public static void transactionsByServiceList(List<List<String>> salesRecords, 
            ArrayList<String> uniqueServices) throws FileNotFoundException{
        for ( String service : uniqueServices){
            String cleanService = cleanString(service).toLowerCase();
            PrintWriter out = new PrintWriter(cleanService+".txt");
            out.println(service.toUpperCase());
            out.printf("\n| %4s | %30s | %10s | %8s |\n", "Line", "Name","Price","Date");
            for (int lineIndex = 0; lineIndex<salesRecords.get(0).size(); lineIndex++){
                String columnValue = salesRecords.get(1).get(lineIndex);
                columnValue = cleanString(columnValue).toUpperCase();
                if (columnValue.equals(service)){
                    String name = salesRecords.get(0).get(lineIndex);
                    if (name.length()>30) name = name.substring(0,27)+"...";
                    Double price = Double.parseDouble((salesRecords.get(2).get(lineIndex)).replaceAll("\\$", ""));
                    String date = salesRecords.get(3).get(lineIndex);
                    out.printf("| %4d | %30.30s | $%-9.2f | %8.8s |\n", lineIndex, name, price, date);
                }
            }
            out.close();
        }
    }
}

