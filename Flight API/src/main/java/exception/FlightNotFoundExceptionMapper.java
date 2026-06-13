/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package exception;

import exception.FlightNotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 *
 * @author Azhaa
 */
@Provider
public class FlightNotFoundExceptionMapper implements ExceptionMapper<FlightNotFoundException> {
    private static final Logger logger = LoggerFactory.getLogger(FlightNotFoundExceptionMapper.class);
    
    @Override
    public Response toResponse(FlightNotFoundException exception) {
        logger.error("Flight not found: {}", exception.getMessage(), exception);
        return Response.status(Response.Status.NOT_FOUND)
                .entity(exception.getMessage())
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
    
}