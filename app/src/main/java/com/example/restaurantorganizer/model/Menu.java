package com.example.restaurantorganizer.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Menu implements Serializable {

    private long id;
    private String name;
    private String ingredients;

}
