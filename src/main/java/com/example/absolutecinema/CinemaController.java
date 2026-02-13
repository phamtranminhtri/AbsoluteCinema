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

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class CinemaController {
    private final CinemaSeats cinemaSeats;
    private Map<String, Seat> bookedSeats;

    public CinemaController(@Autowired CinemaSeats seats) {
        cinemaSeats = seats;

        bookedSeats = new ConcurrentHashMap<>();
    }

    @GetMapping("/seats")
    public ResponseEntity<?> seat() {
        List<Seat> seatList = cinemaSeats.getSeatList();
        List<Seat> availableSeatList = Collections.synchronizedList(new ArrayList<>());
        for (Seat seat : seatList) {
            if (seat.isAvailable()) {
                availableSeatList.add(seat);
            }
        }

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
    public Map<String, ?> purchase(@RequestBody Seat requestedSeat) {
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
        if (!seat.isAvailable()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "The ticket has been already purchased!"
            );
        }

        UUID uuid = UUID.randomUUID();
        seat.setAvailable(false);
        bookedSeats.put(uuid.toString(), seat);

        return Map.of(
                "token", uuid,
                "ticket", seat
        );
    }

    @PostMapping("/return")
    public Map<String, Seat> returnTicket(@RequestBody Map<String, String> body) {
        String token = body.get("token");
        if (!bookedSeats.containsKey(token)) {
            throw new WrongTokenException("Token not found " + token);
        }

        Seat seat = bookedSeats.get(token);
        seat.setAvailable(true);
        bookedSeats.remove(token);

        return Map.of("ticket", seat);
    }

}
