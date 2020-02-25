package com.example.restaurantorganizer.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class Table {

    private long id;
    private String name;
    private List<Seat> seats;

}
