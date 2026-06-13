/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import exception.BookingNotFoundException;
import exception.FlightNotFoundException;
import exception.NoAvailableSeatsException;
import exception.PassengerNotFoundException;
import model.Booking;
import model.Flight;
import model.Passenger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import resource.FlightResource;
import resource.PassengerResource;
/**
 *
 * @author Azhaa
 */
@Path("/bookings")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookingResource {
    private static final Logger logger = LoggerFactory.getLogger(BookingResource.class);

    private static List<Booking> bookings = new ArrayList<>();
    private static int nextBookingId = 1;

    // Get references to flights and passengers from their resource classes
    private static List<Flight> flights = FlightResource.getAllFlightsStatic();
    private static List<Passenger> passengers = PassengerResource.getAllPassengersStatic();

    @POST
    public Response createBooking(Booking bookingRequest) {
        logger.info("POST request to create booking for flight {} and passenger {}",
                bookingRequest.getFlightNumber(), bookingRequest.getPassengerId());

        Flight flight = flights.stream()
                .filter(f -> f.getFlightNumber().equalsIgnoreCase(bookingRequest.getFlightNumber()))
                .findFirst()
                .orElseThrow(() -> new FlightNotFoundException("Flight " + bookingRequest.getFlightNumber() + " not found."));

        Passenger passenger = passengers.stream()
                .filter(p -> p.getPassengerId().equalsIgnoreCase(bookingRequest.getPassengerId()))
                .findFirst()
                .orElseThrow(() -> new PassengerNotFoundException("Passenger " + bookingRequest.getPassengerId() + " not found."));

        if (flight.getAvailableSeats() <= 0) {
            logger.info("No available seats on flight {}", flight.getFlightNumber());
            throw new NoAvailableSeatsException("No available seats on flight " + flight.getFlightNumber());
        }

        String bookingId = "B" + nextBookingId++;
        bookingRequest.setBookingId(bookingId);

        if (bookingRequest.getSeatNumber() == null || bookingRequest.getSeatNumber().trim().isEmpty()) {
            bookingRequest.setSeatNumber(bookingId + "A");  // simple seat assignment
            
            if (bookingRequest.getBookingDate() == null || bookingRequest.getBookingDate().trim().isEmpty()) {
    bookingRequest.setBookingDate(java.time.ZonedDateTime.now(java.time.ZoneOffset.UTC).toString());
}

        }

        flight.setAvailableSeats(flight.getAvailableSeats() - 1);
        bookings.add(bookingRequest);

        logger.info("Booking {} created successfully for passenger {} on flight {}", bookingId, passenger.getPassengerId(), flight.getFlightNumber());

        return Response.status(Response.Status.CREATED).entity(bookingRequest).build();
    }

    @GET
    public List<Booking> getAllBookings() {
        logger.info("GET request for all bookings. Total bookings: {}", bookings.size());
        return bookings;
    }

    @GET
    @Path("/{bookingId}")
    public Booking getBookingById(@PathParam("bookingId") String bookingId) {
        logger.info("GET request for booking with ID {}", bookingId);
        return bookings.stream()
                .filter(b -> b.getBookingId().equalsIgnoreCase(bookingId))
                .findFirst()
                .orElseThrow(() -> new BookingNotFoundException("Booking with ID " + bookingId + " not found."));
    }

    @GET
    @Path("/flights/{flightNumber}")
    public List<Booking> getBookingsByFlight(@PathParam("flightNumber") String flightNumber) {
        logger.info("GET request for bookings on flight {}", flightNumber);

        Flight flight = flights.stream()
                .filter(f -> f.getFlightNumber().equalsIgnoreCase(flightNumber))
                .findFirst()
                .orElseThrow(() -> new FlightNotFoundException("Flight " + flightNumber + " not found."));

        List<Booking> flightBookings = bookings.stream()
                .filter(b -> b.getFlightNumber().equalsIgnoreCase(flightNumber))
                .collect(Collectors.toList());

        logger.info("Found {} bookings for flight {}", flightBookings.size(), flightNumber);
        return flightBookings;
    }

    @GET
    @Path("/passengers/{passengerId}")
    public List<Booking> getBookingsByPassenger(@PathParam("passengerId") String passengerId) {
        logger.info("GET request for bookings for passenger {}", passengerId);

        Passenger passenger = passengers.stream()
                .filter(p -> p.getPassengerId().equalsIgnoreCase(passengerId))
                .findFirst()
                .orElseThrow(() -> new PassengerNotFoundException("Passenger " + passengerId + " not found."));

        List<Booking> passengerBookings = bookings.stream()
                .filter(b -> b.getPassengerId().equalsIgnoreCase(passengerId))
                .collect(Collectors.toList());

        logger.info("Found {} bookings for passenger {}", passengerBookings.size(), passengerId);
        return passengerBookings;
    }

    @DELETE
    @Path("/{bookingId}")
    public Response cancelBooking(@PathParam("bookingId") String bookingId) {
        logger.info("DELETE request to cancel booking {}", bookingId);

        for (int i = 0; i < bookings.size(); i++) {
            Booking b = bookings.get(i);
            if (b.getBookingId().equalsIgnoreCase(bookingId)) {
                Flight flight = flights.stream()
                        .filter(f -> f.getFlightNumber().equalsIgnoreCase(b.getFlightNumber()))
                        .findFirst()
                        .orElse(null);
                if (flight != null) {
                    flight.setAvailableSeats(flight.getAvailableSeats() + 1);
                }
                bookings.remove(i);
                logger.info("Booking {} cancelled successfully.", bookingId);
                return Response.noContent().build();
            }
        }
        logger.info("Booking {} not found for cancellation.", bookingId);
        throw new BookingNotFoundException("Booking with ID " + bookingId + " not found for cancellation.");
    }
}