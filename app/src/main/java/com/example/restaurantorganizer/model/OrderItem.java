package com.example.restaurantorganizer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class OrderItem {

    private long id;
    private Menu menu;
    private int amount;
    private boolean checked;

}
