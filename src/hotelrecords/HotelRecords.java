/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hotelrecords;

import static hotelrecords.Records.readFileToArray;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Tests the Records helper-class.
 *
 * <h5>Class</h5>
 * CS&143 S1
 *
 * <h5>Program/Assignment Title</h5>
 * Project1 File I/O
 *
 * <h5>Date</h5>
 * 1/13/2015
 *
 * @author Matt Bailey    
 */ 
class HotelRecords {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        List<List<String>> records = new ArrayList();
        ArrayList<Integer> linesToSkip = new ArrayList();
        while (records.isEmpty()){
           try{
               records = readFileToArray("records.txt", 4, linesToSkip );
           }catch (BadDataException exception){
               System.err.println("Exception: "+exception.getMsg());
               linesToSkip.add(exception.getLineNumber());
           }catch(FileNotFoundException exception ){
               exception.printStackTrace(System.out);
           }
        }
        ArrayList uniqueServices = Records.getUniqueValues(records, 1);
        Records.totalServices(records, uniqueServices);
        try{
            Records.transactionsByServiceList(records, uniqueServices);
        }catch(FileNotFoundException exception){
            exception.printStackTrace(System.out);
        }
    }
    
}
