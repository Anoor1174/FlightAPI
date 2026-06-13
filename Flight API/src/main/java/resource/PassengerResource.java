/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package resource;


import model.Passenger;
import exception.PassengerNotFoundException;
import exception.InvalidInputException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Azhaa
 */
@Path("/passengers")
public class PassengerResource {
    private static final Logger logger = LoggerFactory.getLogger(PassengerResource.class);
    private static List<Passenger> passengers = new ArrayList<>();
    private static int nextId = 2;

    static {
        passengers.add(new Passenger("1", "Alice", "West", "alice.west@example.com"));
        passengers.add(new Passenger("2", "Bob", "Smith", "bob.smith@example.com"));
    }

    public static List<Passenger> getAllPassengersStatic() {
        return passengers;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPassenger(Passenger passenger) {
        logger.info("POST request to add passenger.");
        if (passenger.getFirstName() == null || passenger.getFirstName().trim().isEmpty()) {
            throw new InvalidInputException("Passenger first name is required.");
        }
        if (passenger.getLastName() == null || passenger.getLastName().trim().isEmpty()) {
            throw new InvalidInputException("Passenger last name is required.");
        }
        if (passenger.getEmail() == null || !passenger.getEmail().matches(".+@.+\\..+")) {
            throw new InvalidInputException("Invalid email address.");
        }
        String id = "3" + nextId++;
        passenger.setPassengerId(id);
        passengers.add(passenger);
        logger.info("Added passenger with ID {}.", id);
        return Response.status(Response.Status.CREATED).entity(passenger).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Passenger> getAllPassengers() {
        logger.info("GET request for all passengers. Count: {}", passengers.size());
        return passengers;
    }

    @GET
    @Path("/{passengerId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Passenger getPassengerById(@PathParam("passengerId") String passengerId) {
        logger.info("GET request for passenger ID: {}", passengerId);
        return passengers.stream()
                .filter(p -> p.getPassengerId().equalsIgnoreCase(passengerId))
                .findFirst()
                .orElseThrow(() -> {
                    logger.info("Passenger {} not found.", passengerId);
                    return new PassengerNotFoundException("Passenger with ID " + passengerId + " not found.");
                });
    }

    @PUT
    @Path("/{passengerId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Passenger updatePassenger(@PathParam("passengerId") String passengerId, Passenger updatedPassenger) {
        logger.info("PUT request to update passenger with ID {}.", passengerId);
        for (int i = 0; i < passengers.size(); i++) {
            Passenger p = passengers.get(i);
            if (p.getPassengerId().equalsIgnoreCase(passengerId)) {
                if (updatedPassenger.getFirstName() == null || updatedPassenger.getFirstName().trim().isEmpty()) {
                    throw new InvalidInputException("Passenger first name is required.");
                }
                if (updatedPassenger.getLastName() == null || updatedPassenger.getLastName().trim().isEmpty()) {
                    throw new InvalidInputException("Passenger last name is required.");
                }
                if (updatedPassenger.getEmail() == null || !updatedPassenger.getEmail().matches(".+@.+\\..+")) {
                    throw new InvalidInputException("Invalid email address.");
                }
                updatedPassenger.setPassengerId(passengerId);
                passengers.set(i, updatedPassenger);
                logger.info("Passenger {} updated successfully.", passengerId);
                return updatedPassenger;
            }
        }
        logger.info("Passenger {} not found for update.", passengerId);
        throw new PassengerNotFoundException("Passenger with ID " + passengerId + " not found for update.");
    }

    @DELETE
    @Path("/{passengerId}")
    public Response deletePassenger(@PathParam("passengerId") String passengerId) {
        logger.info("DELETE request for passenger with ID {}.", passengerId);
        boolean removed = passengers.removeIf(p -> p.getPassengerId().equalsIgnoreCase(passengerId));
        if (!removed) {
            logger.info("Passenger {} not found for deletion.", passengerId);
            throw new PassengerNotFoundException("Passenger with ID " + passengerId + " not found for deletion.");
        }
        logger.info("Passenger {} deleted successfully.", passengerId);
        return Response.noContent().build();
    }
}