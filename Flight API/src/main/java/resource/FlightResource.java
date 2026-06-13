/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package resource;


import model.Flight;
import exception.FlightAlreadyExistsException;
import exception.FlightNotFoundException;
import exception.InvalidFlightCapacityException;
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
@Path("/flights")
public class FlightResource {

    private static final Logger logger = LoggerFactory.getLogger(FlightResource.class);
    public static List<Flight> flights = new ArrayList<>();

    static {
        flights.add(new Flight("BA123", "London", "New York", "2025-12-01T10:00:00Z", "2025-12-01T14:00:00Z", 200, 200));
        flights.add(new Flight("AI456", "Delhi", "Mumbai", "2025-12-02T09:00:00Z", "2025-12-02T11:00:00Z", 150, 150));
    }

    public static List<Flight> getAllFlightsStatic() {
        return flights;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addFlight(Flight flight) {
        logger.info("POST request to create flight: {}", flight.getFlightNumber());

        if (flight.getCapacity() <= 0) {
            throw new InvalidFlightCapacityException("Capacity must be greater than 0.");
        }

        for (Flight f : flights) {
            if (f.getFlightNumber().equalsIgnoreCase(flight.getFlightNumber())) {
                throw new FlightAlreadyExistsException("Flight " + flight.getFlightNumber() + " already exists.");
            }
        }

        flight.setAvailableSeats(flight.getCapacity());
        flights.add(flight);

        logger.info("Flight {} added successfully.", flight.getFlightNumber());

        return Response.status(Response.Status.CREATED)
                       .entity(flight)
                       .build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Flight> getAllFlights() {
        logger.info("GET request for all flights. Number of flights: {}", flights.size());
        return flights;
    }

@GET
@Path("/{flightNumber}")
@Produces(MediaType.APPLICATION_JSON)
public Flight getFlightByNumber(@PathParam("flightNumber") String flightNumber) {
    logger.info("GET request for flight number: {}", flightNumber);

    return flights.stream()
        .filter(f -> f.getFlightNumber().equalsIgnoreCase(flightNumber))
        .findFirst()
        .orElseThrow(() -> {
            logger.info("Flight {} not found.", flightNumber);
            return new FlightNotFoundException("Flight with flightNumber " + flightNumber + " not found.");
        });
}

@GET
@Path("/search")
@Produces(MediaType.APPLICATION_JSON)
public List<Flight> searchFlights(@QueryParam("origin") String origin,
                                  @QueryParam("destination") String destination) {
    logger.info("GET request to search flights with origin: {}, destination: {}", origin, destination);

    List<Flight> results = flights.stream()
        .filter(f -> (origin == null || origin.isEmpty() || f.getOrigin().equalsIgnoreCase(origin)))
        .filter(f -> (destination == null || destination.isEmpty() || f.getDestination().equalsIgnoreCase(destination)))
        .toList();

    logger.info("Search results count: {}", results.size());
    return results;
}


    @PUT
    @Path("/{flightNumber}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Flight updateFlight(@PathParam("flightNumber") String flightNumber, Flight updatedFlight) {
        logger.info("PUT request to update flight: {}", flightNumber);

        for (int i = 0; i < flights.size(); i++) {
            Flight f = flights.get(i);
            if (f.getFlightNumber().equalsIgnoreCase(flightNumber)) {
                updatedFlight.setFlightNumber(flightNumber);

                if (updatedFlight.getCapacity() <= 0) {
                    throw new InvalidFlightCapacityException("Capacity must be greater than 0.");
                }

                if (updatedFlight.getCapacity() != f.getCapacity()) {
                    int capacityDiff = updatedFlight.getCapacity() - f.getCapacity();
                    int newAvailableSeats = f.getAvailableSeats() + capacityDiff;
                    updatedFlight.setAvailableSeats(Math.max(newAvailableSeats, 0));
                } else {
                    updatedFlight.setAvailableSeats(f.getAvailableSeats());
                }

                flights.set(i, updatedFlight);

                logger.info("Flight {} updated successfully.", flightNumber);
                return updatedFlight;
            }
        }

        logger.info("Flight {} not found for update.", flightNumber);
        throw new FlightNotFoundException("Flight with flightNumber " + flightNumber + " not found for update.");
    }

    @DELETE
    @Path("/{flightNumber}")
    public Response deleteFlight(@PathParam("flightNumber") String flightNumber) {
        logger.info("DELETE request for flight: {}", flightNumber);

        for (int i = 0; i < flights.size(); i++) {
            Flight f = flights.get(i);
            if (f.getFlightNumber().equalsIgnoreCase(flightNumber)) {
                // Check for bookings if needed here
                flights.remove(i);

                logger.info("Flight {} deleted successfully.", flightNumber);
                return Response.noContent().build();
            }
        }

        logger.info("Flight {} not found for deletion.", flightNumber);
        throw new FlightNotFoundException("Flight with flightNumber " + flightNumber + " not found for deletion.");
    }
}