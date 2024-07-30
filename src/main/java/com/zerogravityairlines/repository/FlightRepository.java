package com.zerogravityairlines.repository;

import com.zerogravityairlines.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {
    @Query("SELECT DISTINCT f.origin FROM Flight f")
    List<String> findDistinctOriginCities();

    @Query("SELECT DISTINCT f.destination FROM Flight f WHERE f.origin = :origin")
    List<String> findDistinctDestinationCitiesByOrigin(String origin);

    List<Flight> findByOriginAndDestinationAndDate(String origin, String destination, LocalDate date);

    // New method to find flights by origin and destination without date
    List<Flight> findByOriginAndDestination(String origin, String destination);
}

