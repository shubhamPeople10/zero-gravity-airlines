package com.zerogravityairlines.service;

import com.zerogravityairlines.model.Booking;
import com.zerogravityairlines.model.Passenger;
import com.zerogravityairlines.repository.BookingRepository;
import com.zerogravityairlines.repository.PassengerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private PassengerRepository passengerRepository;

    public Booking createBooking(String flightNumber, List<Passenger> passengers) {
        Booking booking = new Booking();
        String confirmationNumber = generateConfirmationNumber();
        booking.setConfirmationNumber(confirmationNumber);
        booking.setFlightNumber(flightNumber);
        booking.setBookingTime(LocalDateTime.now());

        for (Passenger passenger : passengers) {
            passenger.setConfirmationNumber(confirmationNumber);
            passengerRepository.save(passenger);
        }

        booking.setPassengers(passengers);
        return bookingRepository.save(booking);
    }

    private String generateConfirmationNumber() {
        // Generating a serial-like confirmation number
        Long latestBookingId = bookingRepository.count();
        return "BookingID-" + String.format("%03d", latestBookingId + 1);
    }
}