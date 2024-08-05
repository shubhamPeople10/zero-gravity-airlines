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

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
    public ApiResponse<List<CityPair>> getDestinationsByOrigin(@RequestParam @NotBlank(message = "Origin city code is required") String originCityCode) {
        List<CityPair> destinations = flightService.getDestinationsByOrigin(originCityCode);
        return new ApiResponse<>(HttpStatus.OK.value(), destinations, null);
    }

    @GetMapping("/search")
    public ApiResponse<List<Flight>> searchFlights(
            @RequestParam @NotBlank(message = "Origin is required") String originCityCode,
            @RequestParam @NotBlank(message = "Destination is required") String destinationCityCode,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @NotNull(message = "Date is required") LocalDate date,
            @RequestParam(defaultValue = "1") @Min(value = 1, message = "Number of passengers must be at least 1")
            @Max(value = 2, message = "Number of passengers can be at most 2") int numberOfPassengers) {

        validateSearchRequest(originCityCode, destinationCityCode, date);

        List<Flight> flights = flightService.searchFlights(originCityCode, destinationCityCode, date, numberOfPassengers);
        return new ApiResponse<>(HttpStatus.OK.value(), flights, null);
    }

    private void validateSearchRequest(String originCityCode, String destinationCityCode, LocalDate date) {
        if (originCityCode.isEmpty() || destinationCityCode.isEmpty()) {
            throw new CustomException("Origin and Destination cannot be empty", HttpStatus.BAD_REQUEST.value());
        }

        if (date.isBefore(LocalDate.now())) {
            throw new CustomException("Date of journey must be today or a future date", HttpStatus.BAD_REQUEST.value());
        }
    }
}