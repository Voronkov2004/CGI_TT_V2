package org.testtask.cgi_tt.com.testtask.restservice;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalTime;


@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "flights_table")
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String aircraft;
    private String currentLocation;
    private String destination;
    private LocalDate departure;
    private LocalDate arrival;
    private LocalTime departureTime;
    private LocalTime arrivalTime;
    private float flightTime;
    private float firstClassPrice;
    private float economyClassPrice;
    private int seatsTaken;

}

