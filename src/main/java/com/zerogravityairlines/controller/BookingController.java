package com.zerogravityairlines.controller;

import com.zerogravityairlines.model.Booking;
import com.zerogravityairlines.model.BookingRequest;
import com.zerogravityairlines.model.Passenger;
import com.zerogravityairlines.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@Validated
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping
    public Booking createBooking(@RequestBody @Valid BookingRequest bookingRequest) {
        return bookingService.createBooking(bookingRequest.getFlightNumber(), bookingRequest.getPassengers());
    }
}