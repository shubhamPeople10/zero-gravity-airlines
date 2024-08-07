package com.zerogravityairlines.controller;

import com.zerogravityairlines.exception.CustomException;
import com.zerogravityairlines.model.ApiResponse;
import com.zerogravityairlines.model.City;
import com.zerogravityairlines.model.CityPair;
import com.zerogravityairlines.model.Flight;
import com.zerogravityairlines.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/flights")
@Validated
public class FlightController {

    private final FlightService flightService;

    @Autowired
    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping("/city-list")
    public ApiResponse<List<City>> getListOfCity() {
        return flightService.getCityList();
    }

    @GetMapping("/destinations")
    public ApiResponse<List<CityPair>> getDestinationsByOrigin(@RequestParam(required = false) String originCityCode) {
        if (originCityCode == null || originCityCode.trim().isEmpty()) {
            throw new CustomException("Origin city code is required", HttpStatus.BAD_REQUEST.value());
        }
        List<CityPair> destinations = flightService.getDestinationsByOrigin(originCityCode);
        return new ApiResponse<>(HttpStatus.OK.value(), destinations, null);
    }

    @GetMapping("/search")
    public ApiResponse<List<Flight>> searchFlights(
            @RequestParam(required = false) String originCityCode,
            @RequestParam(required = false) String destinationCityCode,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(defaultValue = "1") int numberOfPassengers) {

            validateSearchRequest(originCityCode, destinationCityCode, date, numberOfPassengers);

            List<Flight> flights = flightService.searchFlights(originCityCode, destinationCityCode, date, numberOfPassengers);
            return new ApiResponse<>(HttpStatus.OK.value(), flights, null);
    }

    private void validateSearchRequest(String originCityCode, String destinationCityCode, LocalDate date, int numberOfPassengers) {
        if (originCityCode == null || originCityCode.isEmpty()) {
            throw new CustomException("Origin city code is required", HttpStatus.BAD_REQUEST.value());
        }

        if (destinationCityCode == null || destinationCityCode.isEmpty()) {
            throw new CustomException("Destination city code is required", HttpStatus.BAD_REQUEST.value());
        }

        if (date == null) {
            throw new CustomException("Date of journey is required", HttpStatus.BAD_REQUEST.value());
        }

        if (date.isBefore(LocalDate.now())) {
            throw new CustomException("Date of journey must be today or a future date", HttpStatus.BAD_REQUEST.value());
        }

        if (numberOfPassengers < 1 || numberOfPassengers > 2) {
            throw new CustomException("Number of passengers must be between 1 and 2", HttpStatus.BAD_REQUEST.value());
        }
    }
}