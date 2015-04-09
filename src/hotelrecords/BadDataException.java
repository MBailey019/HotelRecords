/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hotelrecords;

/**
 *
 * @author Matt
 */
class BadDataException extends Exception {
    private Integer lineNumber;
    private String msg;
    
    public BadDataException(Integer lineNumber, String message) {
        this.lineNumber = lineNumber;
        this.msg = message;
    }

    public Integer getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(Integer lineNumber) {
        this.lineNumber = lineNumber;
    }
    public String getMsg() {
        return msg;
    }
    
}
