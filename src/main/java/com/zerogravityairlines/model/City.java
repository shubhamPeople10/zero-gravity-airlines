package com.zerogravityairlines.model;

import javax.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Entity
@Table(name = "city")
@Data
public class City implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "city_name", nullable = false)
    private String cityName;

    @Column(name = "city_code", nullable = false, unique = true)
    private String cityCode;
}