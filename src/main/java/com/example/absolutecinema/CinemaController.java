package com.example.absolutecinema;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CinemaController {
    private final CinemaSeats seats;

    public CinemaController(@Autowired CinemaSeats seats) {
        this.seats = seats;
    }

    @GetMapping("/seats")
    public CinemaSeats seat() {
        return seats;
    }
}
