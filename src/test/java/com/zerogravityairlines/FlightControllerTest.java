package com.zerogravityairlines;

import com.zerogravityairlines.controller.FlightController;
import com.zerogravityairlines.exception.CustomException;
import com.zerogravityairlines.model.ApiResponse;
import com.zerogravityairlines.model.City;
import com.zerogravityairlines.model.CityPair;
import com.zerogravityairlines.model.Flight;
import com.zerogravityairlines.service.FlightService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class FlightControllerTest {

    @Mock
    private FlightService flightService;

    @InjectMocks
    private FlightController flightController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetListOfCity() {
        City city1 = new City();
        city1.setCityCode("DLH");
        city1.setCityName("Delhi");

        City city2 = new City();
        city2.setCityCode("BOM");
        city2.setCityName("Mumbai");

        List<City> mockCityList = new ArrayList<>();
        mockCityList.add(city1);
        mockCityList.add(city2);

        when(flightService.getCityList()).thenReturn(new ApiResponse<>(HttpStatus.OK.value(), mockCityList, null));
        // Act
        ApiResponse<List<City>> response = flightController.getListOfCity();

        // Assert
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals(2, response.getData().size());
        assertEquals("DLH", response.getData().get(0).getCityCode());
    }

    @Test
    public void testGetDestinationsByOrigin() {
        List<CityPair> mockCityPairs = new ArrayList<>();

        City city1 = new City();
        city1.setCityCode("DLH");
        city1.setCityName("Delhi");

        City city2 = new City();
        city2.setCityCode("BOM");
        city2.setCityName("Mumbai");

        CityPair cityPair = new CityPair();
        cityPair.setOriginCity(city1);
        cityPair.setDestinationCity(city2);
        cityPair.setAvailable(true);

        mockCityPairs.add(cityPair);

        when(flightService.getDestinationsByOrigin(anyString())).thenReturn(mockCityPairs);

        ApiResponse<List<CityPair>> response = flightController.getDestinationsByOrigin("DLH");

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals(1, response.getData().size());
        assertEquals("DLH", response.getData().get(0).getOriginCity().getCityCode());
        assertEquals("BOM", response.getData().get(0).getDestinationCity().getCityCode());
    }

    @Test
    public void testSearchFlights() {
        Flight flight1 = new Flight();
        flight1.setFlightNumber("FL001");

        CityPair cityPair = new CityPair();
        cityPair.setId("CP001");
        flight1.setCityPair(cityPair);
        flight1.setPrice(5000);
        flight1.setDate(LocalDate.parse("2024-09-06"));
        flight1.setDepartureTime(LocalDate.now().atStartOfDay().toLocalTime());
        flight1.setArrivalTime(LocalDate.now().atStartOfDay().plusHours(2).toLocalTime());

        List<Flight> mockFlights = new ArrayList<>();
        mockFlights.add(flight1);

        when(flightService.searchFlights("DLH", "BOM", LocalDate.now(), 1))
                .thenReturn(mockFlights);

        ApiResponse<List<Flight>> response = flightController.searchFlights("DLH", "BOM", LocalDate.now(), 1);

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals(1, response.getData().size());
        assertEquals("FL001", response.getData().get(0).getFlightNumber());
    }

    @Test
    public void testSearchFlightsWithInvalidDate() {
        // Act & Assert
        assertThrows(CustomException.class, () -> {
            flightController.searchFlights("DLH", "BOM", LocalDate.now().minusDays(1), 1);
        });
    }

    @Test
    public void testSearchFlightsWithMissingOriginCityCode() {
        // Act & Assert
        assertThrows(CustomException.class, () -> {
            flightController.searchFlights(null, "BOM", LocalDate.now(), 1);
        });
    }

    @Test
    public void testSearchFlightsWithMissingDestinationCityCode() {
        // Act & Assert
        assertThrows(CustomException.class, () -> {
            flightController.searchFlights("DLH", null, LocalDate.now(), 1);
        });
    }

    @Test
    public void testSearchFlightsWithInvalidNumberOfPassengers() {
        // Act & Assert
        assertThrows(CustomException.class, () -> {
            flightController.searchFlights("DLH", "BOM", LocalDate.now(), 0);
        });

        assertThrows(CustomException.class, () -> {
            flightController.searchFlights("DLH", "BOM", LocalDate.now(), 3);
        });
    }
}