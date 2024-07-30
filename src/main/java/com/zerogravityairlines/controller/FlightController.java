package com.zerogravityairlines.controller;

import com.zerogravityairlines.model.CityPair;
import com.zerogravityairlines.model.Flight;
import com.zerogravityairlines.repository.CityPairRepository;
import com.zerogravityairlines.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("/api/flights")
@Validated
public class FlightController {

    @Autowired
    private FlightService flightService;

    @Autowired
    private CityPairRepository cityPairRepository;

    @GetMapping("/city-pairs")
    public List<CityPair> getCityPairs() {
        return cityPairRepository.findAll();
    }

    @GetMapping("/destinations")
    public List<CityPair> getDestinationsByOrigin(@RequestParam String origin) {
        return flightService.getDestinationsByOrigin(origin);
    }

    @GetMapping("/search")
    public List<Flight> searchFlights(
            @RequestParam @NotBlank(message = "Origin is required") String origin,
            @RequestParam @NotBlank(message = "Destination is required") String destination,
            @RequestParam @NotBlank(message = "Date is required") String date,
            @RequestParam(defaultValue = "1") @Min(value = 1, message = "Number of passengers must be at least 1")
            @Max(value = 2, message = "Number of passengers can be at most 2") int numberOfPassengers) {

        return flightService.searchFlights(origin, destination, date, numberOfPassengers);
    }
}