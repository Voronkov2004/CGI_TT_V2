package org.testtask.cgi_tt.com.testtask.restservice;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class FlightRepository {

    private int maxPeopleOnPlane = 366;
    private final JdbcTemplate jdbcTemplate;

    public FlightRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Flight> findAll() {
        return jdbcTemplate.query(
                "SELECT * FROM flights_table",
                (rs, rowNum) -> mapRowToFlight(rs)
        );
    }

    private Flight mapRowToFlight(ResultSet rs) throws SQLException {
        Flight flight = new Flight();
        flight.setId(rs.getLong("id"));
        flight.setAircraft(rs.getString("aircraft"));
        flight.setCurrentLocation(rs.getString("current_location"));
        flight.setDestination(rs.getString("destination"));
        flight.setDeparture(rs.getDate("departure").toLocalDate());
        flight.setArrival(rs.getDate("arrival").toLocalDate());
        flight.setDepartureTime(rs.getTime("departure_time").toLocalTime());
        flight.setArrivalTime(rs.getTime("arrival_time").toLocalTime());
        flight.setFlightTime(rs.getFloat("flight_time"));
        flight.setFirstClassPrice(rs.getFloat("first_class_price"));
        flight.setEconomyClassPrice(rs.getFloat("economy_class_price"));
        flight.setSeatsTaken(rs.getInt("seats_taken"));
        return flight;
    }

    public Flight save(Flight flight) {
        jdbcTemplate.update(
                """
                INSERT INTO flights_table (
                    aircraft, current_location, destination, departure,
                    arrival, departure_time, arrival_time, flight_time,
                    first_class_price, economy_class_price, seats_taken
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """,
                flight.getAircraft(),
                flight.getCurrentLocation(),
                flight.getDestination(),
                flight.getDeparture(),
                flight.getArrival(),
                flight.getDepartureTime(),
                flight.getArrivalTime(),
                flight.getFlightTime(),
                flight.getFirstClassPrice(),
                flight.getEconomyClassPrice(),
                flight.getSeatsTaken()
        );
        return flight;
    }

    public Optional<Flight> allFlights() {
        List<Flight> results = jdbcTemplate.query(
                "SELECT * FROM flights_table",
                (rs, rowNum) -> mapRowToFlight(rs)
        );
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    public Optional<Flight> findById(Long id) {
        List<Flight> results = jdbcTemplate.query(
                "SELECT * FROM flights_table WHERE id = ?",
                (rs, rowNum) -> mapRowToFlight(rs),
                id
        );
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    public List<Flight> findByDestination(String currentLocation, String destination) {
        return jdbcTemplate.query(
                "SELECT * FROM flights_table WHERE current_location = ? AND destination = ?",
                (rs, rowNum) -> mapRowToFlight(rs),
                currentLocation, destination
        );
    }

    public List<Flight> findByDates(LocalDate departure, LocalDate arrival) {
        return jdbcTemplate.query(
                "SELECT * FROM flights_table WHERE departure = ? AND arrival = ?",
                (rs, rowNum) -> mapRowToFlight(rs),
                departure, arrival
        );
    }

    public List<Flight> findByLocationAndTime(String location, String destination, LocalDate departure, LocalDate arrival) {
        return jdbcTemplate.query(
                "SELECT * FROM flights_table WHERE current_location = ? AND destination = ? AND departure = ? AND arrival = ?",
                (rs, rowNum) -> mapRowToFlight(rs),
                location, destination, departure, arrival
        );
    }

    public List<Flight> findByLocationTimeAndPeople(String location, String destination, LocalDate departure, LocalDate arrival, int people) {
        return jdbcTemplate.query(
                "SELECT * FROM flights_table WHERE current_location = ? AND destination = ? AND departure = ? AND arrival = ? AND seats_taken + ? <= ?",
                (rs, rowNum) -> mapRowToFlight(rs),
                location, destination, departure, arrival, people, maxPeopleOnPlane
        );
    }
}
