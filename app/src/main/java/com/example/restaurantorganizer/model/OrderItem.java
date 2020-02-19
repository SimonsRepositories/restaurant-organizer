package com.example.restaurantorganizer.model;

import lombok.Data;

@Data
public class OrderItem {

    private long id;
    private Menu menu;
    private int amount;
    private boolean checked;

}
