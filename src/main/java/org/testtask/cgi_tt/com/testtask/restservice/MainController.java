package org.testtask.cgi_tt.com.testtask.restservice;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
public class MainController {

    private final FlightRepository flightRepository;

    public MainController(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @GetMapping("/flight/all")
    public List<Flight> getAllFlights() {
        List<Flight> flights = flightRepository.findAll();
        if (flights.isEmpty()) {
            throw new RuntimeException("No flights found");
        }
        return flights;
    }

    @GetMapping("/flight/{id}")
    public Flight getFlightById(@PathVariable Long id) {
        return flightRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Flight not found"));
    }

    @GetMapping("/flight/location")
    public List<Flight> getFlightsByLocation(@RequestParam String current, @RequestParam String destination) {
        List<Flight> flights = flightRepository.findByDestination(current, destination);
        if (flights.isEmpty()) {
            throw new RuntimeException("No matching flights found");
        }
        return flights;
    }

    @GetMapping("/flight/date")
    public List<Flight> getFlightsByDates(@RequestParam LocalDate departure, @RequestParam LocalDate arrival) {
        List<Flight> flights = flightRepository.findByDates(departure, arrival);
        if (flights.isEmpty()) {
            throw new RuntimeException("No matching flights found");
        }
        return flights;
    }

    @GetMapping("/flight/locationdate")
    public List<Flight> getFlightsByLocationAndTime(@RequestParam String current,
                                                    @RequestParam String destination,
                                                    @RequestParam LocalDate departure,
                                                    @RequestParam LocalDate arrival) {
        List<Flight> flights = flightRepository.findByLocationAndTime(current, destination, departure, arrival);
        if (flights.isEmpty()) {
            throw new RuntimeException("No matching flights found");
        }
        return flights;
    }

    @GetMapping("/flight/locationdatepeople")
    public List<Flight> getFlightsByLocationTimeAndPeople(@RequestParam String current,
                                                          @RequestParam String destination,
                                                          @RequestParam LocalDate departure,
                                                          @RequestParam LocalDate arrival,
                                                          @RequestParam int people) {
        List<Flight> flights = flightRepository.findByLocationTimeAndPeople(
                current, destination, departure, arrival, people
        );
        if (flights.isEmpty()) {
            throw new RuntimeException("No matching flights found");
        }
        return flights;
    }
}
