package com.zerogravityairlines.model;

import javax.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Entity
@Table(name = "city_pair", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"origin_city_code", "destination_city_code"})})
@Data
public class CityPair implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    @ManyToOne
    @JoinColumn(name = "origin_city_code", referencedColumnName = "city_code", nullable = false)
    private City originCity;

    @ManyToOne
    @JoinColumn(name = "destination_city_code", referencedColumnName = "city_code", nullable = false)
    private City destinationCity;

    @Column(name = "is_available", nullable = false)
    private boolean isAvailable = true;
}