package com.zerogravityairlines.controller;

import com.zerogravityairlines.exception.CustomException;
import com.zerogravityairlines.model.ApiResponse;
import com.zerogravityairlines.model.Booking;
import com.zerogravityairlines.model.BookingRequest;
import com.zerogravityairlines.model.Passenger;
import com.zerogravityairlines.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/bookings")
@Validated
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ApiResponse<Booking> bookFlight(@RequestBody @Valid BookingRequest bookingRequest) {
        validateBookingRequest(bookingRequest);

        Booking booking = bookingService.createBooking(
                bookingRequest.getFlightNumber(),
                bookingRequest.getCityPairId(),
                bookingRequest.getPassengers()
        );

        return new ApiResponse<>(HttpStatus.CREATED.value(), booking, null);
    }

    private void validateBookingRequest(BookingRequest bookingRequest) {
        if (bookingRequest.getFlightNumber() == null ||
                bookingRequest.getCityPairId() == null ||
                bookingRequest.getPassengers() == null ||
                bookingRequest.getNumberOfPassengers() == 0 ||
                bookingRequest.getPassengers().isEmpty()) {

            throw new CustomException("flightNumber, cityPairId, numberOfPassengers, and passengers are required fields", HttpStatus.BAD_REQUEST.value());
        }

        int numberOfPassengers = bookingRequest.getNumberOfPassengers();
        if (numberOfPassengers <= 0) {
            throw new CustomException("Number of passengers must be at least 1", HttpStatus.BAD_REQUEST.value());
        }

        if (numberOfPassengers != bookingRequest.getPassengers().size()) {
            throw new CustomException("Number of passengers does not match the provided passenger details", HttpStatus.BAD_REQUEST.value());
        }

        // Additional validation for passenger details
        for (Passenger passenger : bookingRequest.getPassengers()) {
            if (passenger.getFirstName() == null || passenger.getFirstName().isEmpty()) {
                throw new CustomException("First name is required for all passengers.", HttpStatus.BAD_REQUEST.value());
            }

            if (passenger.getLastName() == null || passenger.getLastName().isEmpty()) {
                throw new CustomException("Last name is required for all passengers.", HttpStatus.BAD_REQUEST.value());
            }

            if (passenger.getAge() == 0 || passenger.getAge() < 18 || passenger.getAge() > 120) {
                throw new CustomException("Age is required field for all passengers and can not less then 18", HttpStatus.BAD_REQUEST.value());
            }

            if (passenger.getGender() == null || passenger.getGender().isEmpty() ||
                    (!passenger.getGender().equals("Male") && !passenger.getGender().equals("Female") && !passenger.getGender().equals("Other"))) {
                throw new CustomException("Gender is required and must be Male, Female, or Other for all passengers.", HttpStatus.BAD_REQUEST.value());
            }

            if (passenger.isPrimary()) {
                if (passenger.getEmail() == null || passenger.getEmail().isEmpty()) {
                    throw new CustomException("Email is required for the primary passenger.", HttpStatus.BAD_REQUEST.value());
                }

                if (passenger.getPhoneNumber() == null || passenger.getPhoneNumber().toString().length() != 10) {
                    throw new CustomException("Phone number is required for the primary passenger and must be 10 digits.", HttpStatus.BAD_REQUEST.value());
                }
            }
        }
    }
}