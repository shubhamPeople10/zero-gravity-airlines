package com.zerogravityairlines.service;

import com.zerogravityairlines.exception.CustomException;
import com.zerogravityairlines.model.Booking;
import com.zerogravityairlines.model.Passenger;
import com.zerogravityairlines.model.Flight;
import com.zerogravityairlines.repository.BookingRepository;
import com.zerogravityairlines.repository.FlightRepository;
import com.zerogravityairlines.repository.PassengerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final PassengerRepository passengerRepository;
    private final FlightRepository flightRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository, PassengerRepository passengerRepository, FlightRepository flightRepository) {
        this.bookingRepository = bookingRepository;
        this.passengerRepository = passengerRepository;
        this.flightRepository = flightRepository;
    }

    public Booking createBooking(String flightNumber, String cityPairId, List<Passenger> passengers) {
        Flight flight = flightRepository.findByFlightNumberAndCityPairId(flightNumber, cityPairId)
                .orElseThrow(() -> new CustomException("Flight not found with the provided flight number and city pair ID", HttpStatus.NOT_FOUND.value()));

        Booking booking = new Booking();
        booking.setConfirmationNumber(generateConfirmationNumber());
        booking.setFlightNumber(flightNumber);
        booking.setBookingTime(LocalDateTime.now());

        Booking savedBooking = bookingRepository.save(booking);

        passengers.forEach(passenger -> {
            passenger.setBooking(savedBooking);
            passengerRepository.save(passenger);
        });

        savedBooking.setPassengers(passengers);
        return savedBooking;
    }

    private String generateConfirmationNumber() {
        Long latestBookingId = bookingRepository.count();
        return "BookingID-" + String.format("%03d", latestBookingId + 1);
    }
}