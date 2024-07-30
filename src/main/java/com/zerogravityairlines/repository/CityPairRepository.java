package com.zerogravityairlines.repository;

import com.zerogravityairlines.model.CityPair;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CityPairRepository extends JpaRepository<CityPair, Long> {
    List<CityPair> findByOrigin(String origin);
}