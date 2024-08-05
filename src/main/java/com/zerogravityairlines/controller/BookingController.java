package com.zerogravityairlines.controller;

import com.zerogravityairlines.exception.CustomException;
import com.zerogravityairlines.model.ApiResponse;
import com.zerogravityairlines.model.Booking;
import com.zerogravityairlines.model.BookingRequest;
import com.zerogravityairlines.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    public ApiResponse<Booking> bookFlight(@RequestBody BookingRequest bookingRequest) {
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
                bookingRequest.getPassengers().isEmpty()) {

            throw new CustomException("flightNumber, cityPairId, and passengers are required fields", HttpStatus.BAD_REQUEST.value());
        }

        int numberOfPassengers = bookingRequest.getNumberOfPassengers();
        if (numberOfPassengers <= 0) {
            throw new CustomException("Number of passengers must be at least 1", HttpStatus.BAD_REQUEST.value());
        }

        if (numberOfPassengers != bookingRequest.getPassengers().size()) {
            throw new CustomException("Number of passengers does not match the provided passenger details", HttpStatus.BAD_REQUEST.value());
        }
    }
}