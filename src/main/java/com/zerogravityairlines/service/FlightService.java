package com.zerogravityairlines.service;

import com.zerogravityairlines.exception.CustomException;
import com.zerogravityairlines.model.ApiResponse;
import com.zerogravityairlines.model.City;
import com.zerogravityairlines.model.CityPair;
import com.zerogravityairlines.model.Flight;
import com.zerogravityairlines.repository.CityPairRepository;
import com.zerogravityairlines.repository.CityRepository;
import com.zerogravityairlines.repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.time.LocalDate;
import java.util.stream.Collectors;

@Service
public class FlightService {

    @Autowired
    private CityPairRepository cityPairRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private FlightRepository flightRepository;

    public ApiResponse<List<City>> getCityList() {
        try {
            List<City> cityList = cityRepository.findAll();
            return new ApiResponse<>(HttpStatus.OK.value(), cityList, null);
        } catch (Exception e) {
            throw new CustomException("Failed to retrieve city list. Please try again later.", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    public List<CityPair> getDestinationsByOrigin(String originCityCode) {
        City originCity = cityRepository.findByCityCode(originCityCode)
                .orElseThrow(() -> new CustomException("City not found for code: " + originCityCode, HttpStatus.NOT_FOUND.value()));
        return cityPairRepository.findByOriginCityAndIsAvailableTrue(originCity);
    }

    public List<Flight> searchFlights(String originCityCode, String destinationCityCode, LocalDate date, int numberOfPassengers) {
        validateCityCode(originCityCode);
        validateCityCode(destinationCityCode);

        CityPair cityPair = cityPairRepository.findByOriginCityCityCodeAndDestinationCityCityCodeAndIsAvailableTrue(originCityCode, destinationCityCode)
                .orElseThrow(() -> new CustomException("No available flights for the selected cities", HttpStatus.NOT_FOUND.value()));

        // Fetch flights for the given city pair and date
        List<Flight> flights = flightRepository.findByCityPairAndDepartureTimeBetween(cityPair, LocalTime.MIDNIGHT, LocalTime.MAX);

        return flights.stream()
                .map(flight -> calculatePriceForPassengers(flight, numberOfPassengers))
                .collect(Collectors.toList());
    }

    private void validateCityCode(String cityCode) {
        cityRepository.findByCityCode(cityCode)
                .orElseThrow(() -> new CustomException("Invalid city code: " + cityCode, HttpStatus.BAD_REQUEST.value()));
    }

    private Flight calculatePriceForPassengers(Flight flight, int numberOfPassengers) {
        flight.setPrice(flight.getPrice() * numberOfPassengers);
        return flight;
    }
}