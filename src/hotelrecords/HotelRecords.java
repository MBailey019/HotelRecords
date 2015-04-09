/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hotelrecords;

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
        try{
          Records.totalServices("records.txt" );
          Records.transactionsByService("records.txt");
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }
    
}
