package com.zerogravityairlines.repository;

import com.zerogravityairlines.model.City;
import com.zerogravityairlines.model.CityPair;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
public interface CityPairRepository extends JpaRepository<CityPair, Long> {
    List<CityPair> findByOriginCityAndIsAvailableTrue(City originCity);

    Optional<CityPair> findByOriginCityCityCodeAndDestinationCityCityCodeAndIsAvailableTrue(String originCityCode, String destinationCityCode);
}