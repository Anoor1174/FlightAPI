/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package exception;

import exception.BadRequestException;

/**
 *
 * @author Azhaa
 */
public class InvalidFlightCapacityException extends BadRequestException {
    
    public InvalidFlightCapacityException(String message) {
        super(message);
    }
}