package com.example.absolutecinema;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class CinemaSeats {
    private int rows;
    private int columns;
    @JsonProperty("seats")
    private List<Seat> seatList;

    public CinemaSeats() {
        this.rows = 9;
        this.columns = 9;

        this.seatList = Collections.synchronizedList(new ArrayList<>());
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                seatList.add(new Seat(i + 1, j + 1));
            }
        }
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public List<Seat> getSeatList() {
        return seatList;
    }

    public void setSeatList(List<Seat> seatList) {
        this.seatList = seatList;
    }


}
