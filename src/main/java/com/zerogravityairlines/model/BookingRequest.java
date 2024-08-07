package com.zerogravityairlines.model;

import java.util.List;
import lombok.Data;

import javax.validation.Valid;

@Data
public class BookingRequest {
    private String flightNumber;
    private String cityPairId;
    private int numberOfPassengers;

    @Valid
    private List<Passenger> passengers;
}