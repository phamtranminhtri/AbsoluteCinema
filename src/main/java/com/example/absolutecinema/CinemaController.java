package com.example.absolutecinema;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class CinemaController {
    private final CinemaSeats cinemaSeats;
    private List<Seat> availableSeatList;

    public CinemaController(@Autowired CinemaSeats seats) {
        this.cinemaSeats = seats;
        List<Seat> seatList = cinemaSeats.getSeatList();
        availableSeatList = new ArrayList<>();
        availableSeatList.addAll(seatList);
    }

    @GetMapping("/seats")
    public ResponseEntity<?> seat() {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of(
                        "rows", cinemaSeats.getRows(),
                        "columns", cinemaSeats.getColumns(),
                        "seats", availableSeatList
                ));
    }

    @PostMapping("/purchase")
    public Seat purchase(@RequestBody Seat requestedSeat) {
        int row = requestedSeat.getRow() - 1;
        int column = requestedSeat.getColumn() - 1;
        int numRow = cinemaSeats.getRows();
        int numCol = cinemaSeats.getColumns();

        if (row < 0 || row >= numRow || column < 0 || column >= numCol) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "The number of a row or a column is out of bounds!"
            );
        }

        Seat seat = cinemaSeats.getSeatList().get(row * numCol + column);
        if (seat.isAvailable()) {
            seat.setAvailable(false);
            availableSeatList.remove(seat);
            return seat;
        } else {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "The ticket has been already purchased!"
            );
        }
    }
}
