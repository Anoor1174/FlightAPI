/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package exception;

import exception.ErrorResponse;
import exception.InvalidInputException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Azhaa
 */
@Provider 
public class InvalidInputExceptionMapper implements ExceptionMapper<InvalidInputException> {
    private static final Logger logger = LoggerFactory.getLogger(InvalidInputExceptionMapper.class); 
    
   
    @Override 
    public Response toResponse(InvalidInputException exception){
        logger.error("Invalid input: {}", exception.getMessage(), exception); 
        
        //builds and returns a HTTP 400 response
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorResponse("Invalid Input", exception.getMessage()))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}