package com.zerogravityairlines.model;

import java.util.List;
import lombok.Data;

@Data
public class BookingRequest {
    private String flightNumber;
    private String cityPairId;
    private int numberOfPassengers;
    private List<Passenger> passengers;
}