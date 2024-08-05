package com.zerogravityairlines.repository;

import com.zerogravityairlines.model.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
    List<City> findAll();

    Optional<City> findByCityCode(String cityCode);
}