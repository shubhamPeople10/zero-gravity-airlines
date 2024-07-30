package com.zerogravityairlines.service;

import com.zerogravityairlines.exception.NoDirectFlightException;
import com.zerogravityairlines.model.CityPair;
import com.zerogravityairlines.model.Flight;
import com.zerogravityairlines.repository.CityPairRepository;
import com.zerogravityairlines.repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.time.LocalDate;
import java.util.stream.Collectors;

@Service
public class FlightService {

    @Autowired
    private CityPairRepository cityPairRepository;

    @Autowired
    private FlightRepository flightRepository;

    public List<CityPair> getDestinationsByOrigin(String origin) {
        return cityPairRepository.findByOrigin(origin);
    }

    public List<Flight> searchFlights(String origin, String destination, String date, int numberOfPassengers) {
        LocalDate localDate = LocalDate.parse(date);

        // Check if the city pair exists in the database
        List<Flight> allFlightsForCityPair = flightRepository.findByOriginAndDestination(origin, destination);
        if (allFlightsForCityPair.isEmpty()) {
            throw new NoDirectFlightException("There is No direct flight between " + origin + " and " + destination + ". Please select a different city pair.");
        }

        // Check for flights on the specific date
        List<Flight> flights = flightRepository.findByOriginAndDestinationAndDate(origin, destination, localDate);
        if (flights.isEmpty()) {
            throw new NoDirectFlightException("No direct flight between " + origin + " and " + destination + " on " + date);
        }

        return flights.stream()
                .map(flight -> calculatePriceForPassengers(flight, numberOfPassengers))
                .collect(Collectors.toList());
    }

    private Flight calculatePriceForPassengers(Flight flight, int numberOfPassengers) {
        flight.setTicketPrice(flight.getTicketPrice() * numberOfPassengers);
        return flight;
    }
}