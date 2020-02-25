package com.example.restaurantorganizer.model;

import lombok.Data;

@Data
public class Menu {

    private long id;
    private String name;
    private String ingredients;
    private Menutype menutype;

}
