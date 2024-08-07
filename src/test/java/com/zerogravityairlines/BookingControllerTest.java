package com.zerogravityairlines;

import com.zerogravityairlines.controller.BookingController;
import com.zerogravityairlines.exception.CustomException;
import com.zerogravityairlines.model.ApiResponse;
import com.zerogravityairlines.model.Booking;
import com.zerogravityairlines.model.BookingRequest;
import com.zerogravityairlines.model.Passenger;
import com.zerogravityairlines.service.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class BookingControllerTest {

    @InjectMocks
    private BookingController bookingController;

    @Mock
    private BookingService bookingService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testBookFlight_Success() {
        // Arrange
        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setFlightNumber("FL001");
        bookingRequest.setCityPairId("CP001");
        bookingRequest.setNumberOfPassengers(1);

        Passenger passenger = new Passenger();
        passenger.setFirstName("Shubham");
        passenger.setLastName("Sharma");
        passenger.setAge(30);
        passenger.setGender("Male");
        passenger.setEmail("shubham@example.com");
        passenger.setPhoneNumber("1234567890");
        passenger.setPrimary(true);

        bookingRequest.setPassengers(Collections.singletonList(passenger));

        Booking booking = new Booking();
        booking.setConfirmationNumber("BookingID-001");

        when(bookingService.createBooking(any(), any(), any())).thenReturn(booking);

        // Act
        ApiResponse<Booking> response = bookingController.bookFlight(bookingRequest);

        // Assert
        assertEquals(HttpStatus.CREATED.value(), response.getStatusCode());
        assertEquals("BookingID-001", response.getData().getConfirmationNumber());
    }

    @Test
    public void testBookFlight_BadRequest_MissingFields() {
        // Arrange
        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setFlightNumber("FL001");

        // Act & Assert
        CustomException thrown = assertThrows(CustomException.class, () -> {
            bookingController.bookFlight(bookingRequest);
        });

        assertEquals("flightNumber, cityPairId, numberOfPassengers, and passengers are required fields", thrown.getMessage());
    }

    @Test
    public void testBookFlight_BadRequest_InvalidNumberOfPassengers() {
        // Arrange
        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setFlightNumber("FL001");
        bookingRequest.setCityPairId("CP001");
        bookingRequest.setNumberOfPassengers(2);

        // Create Passenger using default constructor and setters
        Passenger passenger = new Passenger();
        passenger.setFirstName("Shubham");
        passenger.setLastName("Sharma");
        passenger.setAge(30);

        bookingRequest.setPassengers(Collections.singletonList(passenger));

        // Act & Assert
        CustomException thrown = assertThrows(CustomException.class, () -> {
            bookingController.bookFlight(bookingRequest);
        });

        assertEquals("Number of passengers does not match the provided passenger details", thrown.getMessage());
    }

    @Test
    public void testBookFlight_BadRequest_InvalidPassengerDetails() {
        // Arrange
        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setFlightNumber("FL001");
        bookingRequest.setCityPairId("CP001");
        bookingRequest.setNumberOfPassengers(1);

        // Create Passenger with missing email and phone number
        Passenger passenger = new Passenger();
        passenger.setFirstName("Shubham");
        passenger.setLastName("Sharma");
        passenger.setAge(30);
        passenger.setGender("Male");
        passenger.setPrimary(true);

        bookingRequest.setPassengers(Collections.singletonList(passenger));

        // Act & Assert
        CustomException thrown = assertThrows(CustomException.class, () -> {
            bookingController.bookFlight(bookingRequest);
        });

        assertEquals("Email is required for the primary passenger.", thrown.getMessage());
    }

    @Test
    public void testBookFlight_BadRequest_InvalidPassengerAge() {
        // Arrange
        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setFlightNumber("FL001");
        bookingRequest.setCityPairId("CP001");
        bookingRequest.setNumberOfPassengers(1);

        // Create Passenger with invalid age
        Passenger passenger = new Passenger();
        passenger.setFirstName("Shubham");
        passenger.setLastName("Sharma");
        passenger.setAge(17); // Invalid age

        bookingRequest.setPassengers(Collections.singletonList(passenger));

        // Act & Assert
        CustomException thrown = assertThrows(CustomException.class, () -> {
            bookingController.bookFlight(bookingRequest);
        });

        assertEquals("Age is required field for all passengers and can not less then 18", thrown.getMessage());
    }

    @Test
    public void testBookFlight_BadRequest_InvalidPassengerGender() {
        // Arrange
        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setFlightNumber("FL001");
        bookingRequest.setCityPairId("CP001");
        bookingRequest.setNumberOfPassengers(1);

        // Create Passenger with invalid gender
        Passenger passenger = new Passenger();
        passenger.setFirstName("Shubham");
        passenger.setLastName("Sharma");
        passenger.setAge(30);
        passenger.setGender("Unknown"); // Invalid gender

        bookingRequest.setPassengers(Collections.singletonList(passenger));

        // Act & Assert
        CustomException thrown = assertThrows(CustomException.class, () -> {
            bookingController.bookFlight(bookingRequest);
        });

        assertEquals("Gender is required and must be Male, Female, or Other for all passengers.", thrown.getMessage());
    }

    @Test
    public void testBookFlight_BadRequest_InvalidPhoneNumber() {
        // Arrange
        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setFlightNumber("FL001");
        bookingRequest.setCityPairId("CP001");
        bookingRequest.setNumberOfPassengers(1);

        // Create Passenger with invalid phone number
        Passenger passenger = new Passenger();
        passenger.setFirstName("Shubham");
        passenger.setLastName("Sharma");
        passenger.setAge(30);
        passenger.setGender("Male");
        passenger.setEmail("shubham@example.com");
        passenger.setPhoneNumber("123456"); // Invalid phone number
        passenger.setPrimary(true);

        bookingRequest.setPassengers(Collections.singletonList(passenger));

        // Act & Assert
        CustomException thrown = assertThrows(CustomException.class, () -> {
            bookingController.bookFlight(bookingRequest);
        });

        assertEquals("Phone number is required for the primary passenger and must be 10 digits.", thrown.getMessage());
    }
}