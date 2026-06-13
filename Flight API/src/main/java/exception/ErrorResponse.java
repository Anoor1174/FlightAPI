/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package exception;

/**
 *
 * @author Azhaa
 */
public class ErrorResponse {
    
    //private fields to store error type and message 
    private String error; 
    private String message; 

    //ErrorResponse constructor
    public ErrorResponse(String error, String message) {
        this.error = error;
        this.message = message;
    }

    //retrieves error type 
    public String getError() {
        return error;
    }

    //sets error type 
    public void setError(String error) {
        this.error = error;
    }
    
    //retrieves error message 
    public String getMessage() {
        return message;
    }

    //sets error message 
    public void setMessage(String message) {
        this.message = message;
    }
}
