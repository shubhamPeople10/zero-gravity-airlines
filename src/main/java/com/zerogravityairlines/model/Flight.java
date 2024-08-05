package com.zerogravityairlines.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Data;

@Entity
@Table(name = "flight", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"flightNumber", "city_pair_id"})
})
@Data
public class Flight implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String flightNumber;
    private LocalDate date;
    private LocalTime departureTime;
    private LocalTime arrivalTime;
    private double price;
    @ManyToOne
    @JoinColumn(name = "city_pair_id")
    private CityPair cityPair;
}