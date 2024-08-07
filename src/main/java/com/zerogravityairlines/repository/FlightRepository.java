package com.zerogravityairlines.repository;

import com.zerogravityairlines.model.CityPair;
import com.zerogravityairlines.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {
    List<Flight> findByCityPairAndDateAndDepartureTimeBetween(CityPair cityPair, LocalDate date, LocalTime startTime, LocalTime endTime);
    Optional<Flight> findByFlightNumberAndCityPairId(String flightNumber, String cityPairId);
}

