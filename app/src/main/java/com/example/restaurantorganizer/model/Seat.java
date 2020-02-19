package com.example.restaurantorganizer.model;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
public class Seat {

    private long id;
    private List<OrderItem> orderItems = new ArrayList<>();



}
