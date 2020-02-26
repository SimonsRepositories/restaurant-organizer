package com.example.restaurantorganizer.model;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class Seat implements Serializable {

    private long id;
    private List<OrderItem> orderItems;
}
