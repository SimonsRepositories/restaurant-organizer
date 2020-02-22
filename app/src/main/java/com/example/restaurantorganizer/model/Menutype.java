package com.example.restaurantorganizer.model;

import java.util.ArrayList;
import java.util.List;

public class Menutype
{
    private long id;
    private String name;
    private List<Item> allItems;

    public Menutype(long id, String name, List<Item> allItems) {
        this.id = id;
        this.name = name;
        this.allItems = allItems;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Item> getAllItems() {
        return allItems;
    }

    public void setAllItems(List<Item> allItems) {
        this.allItems = allItems;
    }
}
