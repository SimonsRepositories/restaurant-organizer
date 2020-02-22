package com.example.restaurantorganizer.model;

public class Item
{
    public long id;
    public String title;
    public String ingredients;

    public Item(long id, String title, String ingredients) {
        this.id = id;
        this.title = title;
        this.ingredients = ingredients;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }
}
